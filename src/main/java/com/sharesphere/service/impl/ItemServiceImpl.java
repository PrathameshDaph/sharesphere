package com.sharesphere.service.impl;

import com.sharesphere.dto.request.ItemRequest;
import com.sharesphere.dto.response.*;
import com.sharesphere.entity.*;
import com.sharesphere.exception.*;
import com.sharesphere.repository.*;
import com.sharesphere.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserServiceImpl userService;

    @Override
    public ItemResponse createItem(ItemRequest req, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Item item = Item.builder()
                .name(req.getName()).description(req.getDescription())
                .category(category).condition(req.getCondition())
                .price(req.getPrice()).rentalPricePerDay(req.getRentalPricePerDay())
                .securityDeposit(req.getSecurityDeposit())
                .availableQuantity(req.getAvailableQuantity())
                .location(req.getLocation()).listingType(req.getListingType())
                .owner(owner).build();
        return toResponse(itemRepository.save(item), ownerId);
    }

    @Override
    @Transactional
    public ItemResponse getItemById(Long itemId, Long currentUserId) {
        Item item = findItem(itemId);
        item.setViewCount(item.getViewCount() + 1);
        itemRepository.save(item);
        return toResponse(item, currentUserId);
    }

    @Override
    public ItemResponse updateItem(Long itemId, ItemRequest req, Long currentUserId) {
        Item item = findItem(itemId);
        if (!item.getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You can only edit your own listings");
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        item.setName(req.getName()); item.setDescription(req.getDescription());
        item.setCategory(category); item.setCondition(req.getCondition());
        item.setPrice(req.getPrice()); item.setRentalPricePerDay(req.getRentalPricePerDay());
        item.setSecurityDeposit(req.getSecurityDeposit());
        item.setAvailableQuantity(req.getAvailableQuantity());
        item.setLocation(req.getLocation()); item.setListingType(req.getListingType());
        return toResponse(itemRepository.save(item), currentUserId);
    }

    @Override
    public void deleteItem(Long itemId, Long currentUserId) {
        Item item = findItem(itemId);
        if (!item.getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("You can only delete your own listings");
        itemRepository.delete(item);
    }

    @Override
    public void adminDeleteItem(Long itemId) {
        itemRepository.delete(findItem(itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ItemResponse> searchItems(String keyword, Long categoryId,
            ListingType listingType, String condition, String location,
            BigDecimal minPrice, BigDecimal maxPrice, String sortBy, int page, int size, Long currentUserId) {
        Sort sort = switch (sortBy == null ? "relevance" : sortBy.toLowerCase()) {
            case "price_asc"  -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "newest"     -> Sort.by("createdAt").descending();
            case "popular"    -> Sort.by("viewCount").descending();
            default           -> Sort.by("createdAt").descending();
        };
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> resultPage = itemRepository.searchItems(
                keyword, categoryId, listingType, condition, location, minPrice, maxPrice, pageable);
        List<ItemResponse> content = resultPage.getContent().stream()
                .map(i -> toResponse(i, currentUserId)).toList();
        return PagedResponse.<ItemResponse>builder()
                .content(content).page(page).size(size)
                .totalElements(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .last(resultPage.isLast()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getMyListings(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(i -> toResponse(i, ownerId)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getPopularItems() {
        return itemRepository.findPopularItems(PageRequest.of(0, 8)).stream()
                .map(i -> toResponse(i, null)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getLatestItems() {
        return itemRepository.findLatestItems(PageRequest.of(0, 12)).stream()
                .map(i -> toResponse(i, null)).toList();
    }

    @Override
    @Transactional
    public void addImage(Long itemId, String imageUrl, Long currentUserId) {
        Item item = findItem(itemId);
        if (!item.getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("Not your item");
        item.getImages().add(ItemImage.builder().item(item).imageUrl(imageUrl).build());
        itemRepository.save(item);
    }

    @Override
    public void incrementViewCount(Long itemId) {
        Item item = findItem(itemId);
        item.setViewCount(item.getViewCount() + 1);
        itemRepository.save(item);
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));
    }

    public ItemResponse toResponse(Item i, Long currentUserId) {
        boolean fav = currentUserId != null && favoriteRepository.existsByUserIdAndItemId(currentUserId, i.getId());
        List<String> imageUrls = i.getImages().stream().map(ItemImage::getImageUrl).toList();
        CategoryResponse cat = i.getCategory() != null
                ? CategoryResponse.builder().id(i.getCategory().getId())
                  .name(i.getCategory().getName()).icon(i.getCategory().getIcon())
                  .description(i.getCategory().getDescription()).build()
                : null;
        return ItemResponse.builder()
                .id(i.getId()).name(i.getName()).description(i.getDescription())
                .category(cat).condition(i.getCondition())
                .price(i.getPrice()).rentalPricePerDay(i.getRentalPricePerDay())
                .securityDeposit(i.getSecurityDeposit())
                .availableQuantity(i.getAvailableQuantity()).location(i.getLocation())
                .listingType(i.getListingType()).status(i.getStatus())
                .owner(userService.toResponse(i.getOwner()))
                .imageUrls(imageUrls).averageRating(i.getAverageRating())
                .totalRatings(i.getTotalRatings()).viewCount(i.getViewCount())
                .createdAt(i.getCreatedAt()).favorited(fav).build();
    }
}
