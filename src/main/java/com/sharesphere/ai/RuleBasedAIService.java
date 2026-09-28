package com.sharesphere.ai;

import com.sharesphere.dto.response.AISearchResponse;
import com.sharesphere.dto.response.ItemResponse;
import com.sharesphere.entity.ListingType;
import com.sharesphere.repository.*;
import com.sharesphere.service.impl.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.*;

@Service @RequiredArgsConstructor @Slf4j
public class RuleBasedAIService implements AIRecommendationService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final FavoriteRepository favoriteRepository;
    private final ItemServiceImpl itemService;

    // keyword -> category name mapping
    private static final Map<String, String> KEYWORD_CATEGORY = new LinkedHashMap<>() {{
        put("calculator|casio|fx-991|scientific", "Calculators");
        put("arduino|raspberry|microcontroller|iot|sensor|breadboard", "Electronics");
        put("laptop|phone|mobile|camera|dslr|speaker|headphone|charger|tablet|drone", "Electronics");
        put("book|textbook|novel|reference|programming", "Books");
        put("drawing|compass|set square|drafting|engineering tool|protractor", "Engineering Tools");
        put("lab|beaker|microscope|experiment|circuit|oscilloscope", "Lab Equipment");
        put("cycle|bicycle|bike", "Cycles");
        put("football|cricket|bat|racket|badminton|tennis|sport|gym|dumbbell", "Sports Equipment");
        put("chair|table|desk|mattress|lamp|shelf|furniture|almirah", "Furniture");
        put("bag|backpack|luggage|suitcase", "Bags");
        put("guitar|keyboard|drums|violin|instrument|music", "Musical Instruments");
        put("projector|mic|speaker|event|display|banner", "Event Equipment");
        put("hostel|bucket|mug|plate|kettle|iron|fan", "Hostel Items");
        put("notes|assignment|material|question paper|study", "Study Materials");
    }};

    @Override
    public AISearchResponse parseAndSearch(String query, Long currentUserId) {
        String q = query.toLowerCase();

        // Detect listing type preference
        ListingType preferredType = null;
        Integer durationDays = null;
        if (q.matches(".*\\b(rent|borrow|for \\d+ day|temporary|few days|week).*")) {
            preferredType = ListingType.RENT;
        } else if (q.matches(".*\\b(buy|purchase|sell|second.?hand|used).*")) {
            preferredType = ListingType.SELL;
        }

        // Extract duration
        Pattern durationPattern = Pattern.compile("(\\d+)\\s*day");
        Matcher dm = durationPattern.matcher(q);
        if (dm.find()) {
            durationDays = Integer.parseInt(dm.group(1));
            if (preferredType == null) preferredType = ListingType.RENT;
        }

        // Detect location
        String location = null;
        for (String loc : List.of("hostel", "library", "lab", "canteen", "campus", "block a", "block b")) {
            if (q.contains(loc)) { location = loc; break; }
        }

        // Detect budget
        BigDecimal maxPrice = null;
        Pattern pricePattern = Pattern.compile("under[\\s₹]*(\\d+)|budget[\\s₹]*(\\d+)|less than[\\s₹]*(\\d+)");
        Matcher pm = pricePattern.matcher(q);
        if (pm.find()) {
            String priceStr = pm.group(1) != null ? pm.group(1) : pm.group(2) != null ? pm.group(2) : pm.group(3);
            if (priceStr != null) maxPrice = new BigDecimal(priceStr);
        }

        // Detect category
        String detectedCategory = null;
        for (Map.Entry<String, String> entry : KEYWORD_CATEGORY.entrySet()) {
            if (Pattern.compile(entry.getKey()).matcher(q).find()) {
                detectedCategory = entry.getValue();
                break;
            }
        }

        // Find category ID
        Long categoryId = null;
        if (detectedCategory != null) {
            String cat = detectedCategory;
            categoryId = categoryRepository.findAll().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(cat))
                    .map(c -> c.getId()).findFirst().orElse(null);
        }

        // Extract main keyword (remove stop words)
        String keyword = q.replaceAll("\\b(i need|i want|looking for|find me|for|a|an|the|to|my|me|need|want|give|get)\\b", "")
                .replaceAll("\\d+ days?", "").replaceAll("near \\w+", "").trim();
        if (keyword.length() < 2) keyword = null;

        // Search
        var results = itemRepository.searchItems(keyword, categoryId, preferredType,
                null, location, null, maxPrice,
                PageRequest.of(0, 10)).getContent();

        List<ItemResponse> items = results.stream()
                .map(i -> itemService.toResponse(i, currentUserId)).toList();

        // Generate suggestion text
        String suggestion = generateSuggestion(q, detectedCategory, preferredType, durationDays, items.size());

        return AISearchResponse.builder()
                .originalQuery(query).detectedCategory(detectedCategory)
                .detectedListingType(preferredType).detectedDurationDays(durationDays)
                .detectedLocation(location).aiSuggestion(suggestion).results(items).build();
    }

    @Override
    public List<ItemResponse> getPersonalizedRecommendations(Long userId) {
        // Get recent search queries
        List<String> recentQueries = searchHistoryRepository.findRecentQueriesByUserId(userId)
                .stream().limit(5).toList();

        // Build keyword from history
        String combinedKeyword = recentQueries.isEmpty() ? null :
                String.join(" ", recentQueries).substring(0, Math.min(50, String.join(" ", recentQueries).length()));

        // Get favorites categories
        var favorites = favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId);
        Long favCategoryId = favorites.isEmpty() ? null :
                favorites.get(0).getItem().getCategory() != null
                        ? favorites.get(0).getItem().getCategory().getId() : null;

        // Search with history-based terms
        if (combinedKeyword != null && !combinedKeyword.isBlank()) {
            return itemRepository.searchItems(combinedKeyword, favCategoryId, null,
                    null, null, null, null, PageRequest.of(0, 6))
                    .getContent().stream().map(i -> itemService.toResponse(i, userId)).toList();
        }
        // Fallback: popular items
        return itemRepository.findPopularItems(PageRequest.of(0, 6)).stream()
                .map(i -> itemService.toResponse(i, userId)).toList();
    }

    @Override
    public List<ItemResponse> getSimilarItems(Long itemId, Long currentUserId) {
        return itemRepository.findById(itemId).map(item -> {
            Long catId = item.getCategory() != null ? item.getCategory().getId() : null;
            return itemRepository.searchItems(null, catId, item.getListingType(),
                    null, null, null, null, PageRequest.of(0, 6)).getContent()
                    .stream().filter(i -> !i.getId().equals(itemId))
                    .map(i -> itemService.toResponse(i, currentUserId)).toList();
        }).orElse(List.of());
    }

    @Override
    public String generateSearchSuggestion(String query) {
        return generateSuggestion(query.toLowerCase(), null, null, null, 0);
    }

    private String generateSuggestion(String q, String category, ListingType type,
                                       Integer days, int resultCount) {
        StringBuilder sb = new StringBuilder();
        if (type == ListingType.RENT && days != null) {
            sb.append("💡 Renting for ").append(days).append(" days is a smart choice! ");
        } else if (type == ListingType.RENT) {
            sb.append("💡 Renting saves money for short-term use. ");
        } else if (type == ListingType.SELL) {
            sb.append("💡 Looking to buy second-hand saves up to 70%! ");
        } else {
            sb.append("💡 Consider renting if you only need this temporarily. ");
        }
        if (category != null) sb.append("Showing items in ").append(category).append(". ");
        sb.append("Found ").append(resultCount).append(" matching item").append(resultCount != 1 ? "s" : "").append(".");
        return sb.toString();
    }
}
