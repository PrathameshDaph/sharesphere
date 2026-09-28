package com.sharesphere.controller;

import com.sharesphere.dto.request.ItemRequest;
import com.sharesphere.dto.response.*;
import com.sharesphere.entity.ListingType;
import com.sharesphere.entity.User;
import com.sharesphere.service.FileStorageService;
import com.sharesphere.service.ItemService;
import com.sharesphere.service.SearchHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final FileStorageService fileStorageService;
    private final SearchHistoryService searchHistoryService;

    @PostMapping
    public ResponseEntity<ItemResponse> create(@Valid @RequestBody ItemRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(itemService.createItem(req, ((User) ud).getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getOne(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        Long userId = ud != null ? ((User) ud).getId() : null;
        return ResponseEntity.ok(itemService.getItemById(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(@PathVariable Long id,
            @Valid @RequestBody ItemRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(itemService.updateItem(id, req, ((User) ud).getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        itemService.deleteItem(id, ((User) ud).getId());
        return ResponseEntity.ok(Map.of("message", "Item deleted"));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<ItemResponse>> search(
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false) Long categoryId,
            @RequestParam(required=false) ListingType listingType,
            @RequestParam(required=false) String condition,
            @RequestParam(required=false) String location,
            @RequestParam(required=false) BigDecimal minPrice,
            @RequestParam(required=false) BigDecimal maxPrice,
            @RequestParam(defaultValue="newest") String sortBy,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size,
            @AuthenticationPrincipal UserDetails ud) {
        Long userId = ud != null ? ((User) ud).getId() : null;
        if (keyword != null && userId != null) searchHistoryService.record(userId, keyword);
        return ResponseEntity.ok(itemService.searchItems(keyword, categoryId, listingType,
                condition, location, minPrice, maxPrice, sortBy, page, size, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ItemResponse>> myListings(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(itemService.getMyListings(((User) ud).getId()));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ItemResponse>> popular() {
        return ResponseEntity.ok(itemService.getPopularItems());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ItemResponse>> latest() {
        return ResponseEntity.ok(itemService.getLatestItems());
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<Map<String,String>> uploadImage(@PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails ud) {
        String url = fileStorageService.storeFile(file);
        itemService.addImage(id, url, ((User) ud).getId());
        return ResponseEntity.ok(Map.of("imageUrl", url));
    }
}
