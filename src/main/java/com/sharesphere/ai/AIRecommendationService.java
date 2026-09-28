package com.sharesphere.ai;

import com.sharesphere.dto.response.AISearchResponse;
import com.sharesphere.dto.response.ItemResponse;
import java.util.List;

public interface AIRecommendationService {
    AISearchResponse parseAndSearch(String query, Long currentUserId);
    List<ItemResponse> getPersonalizedRecommendations(Long userId);
    List<ItemResponse> getSimilarItems(Long itemId, Long currentUserId);
    String generateSearchSuggestion(String query);
}
