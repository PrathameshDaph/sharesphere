package com.sharesphere.service;

import com.sharesphere.dto.request.ItemRequest;
import com.sharesphere.dto.response.ItemResponse;
import com.sharesphere.dto.response.PagedResponse;
import com.sharesphere.entity.ListingType;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ItemService {
    ItemResponse createItem(ItemRequest request, Long ownerId);
    ItemResponse getItemById(Long itemId, Long currentUserId);
    ItemResponse updateItem(Long itemId, ItemRequest request, Long currentUserId);
    void deleteItem(Long itemId, Long currentUserId);
    PagedResponse<ItemResponse> searchItems(String keyword, Long categoryId, ListingType listingType,
            String condition, String location, BigDecimal minPrice, BigDecimal maxPrice,
            String sortBy, int page, int size, Long currentUserId);
    List<ItemResponse> getMyListings(Long ownerId);
    List<ItemResponse> getPopularItems();
    List<ItemResponse> getLatestItems();
    void addImage(Long itemId, String imageUrl, Long currentUserId);
    void incrementViewCount(Long itemId);
    void adminDeleteItem(Long itemId);
}
