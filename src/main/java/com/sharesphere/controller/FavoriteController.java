package com.sharesphere.controller;

import com.sharesphere.dto.response.FavoriteResponse;
import com.sharesphere.entity.User;
import com.sharesphere.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getAll(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(favoriteService.getMyFavorites(((User)ud).getId()));
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<FavoriteResponse> add(@PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(favoriteService.addFavorite(itemId, ((User)ud).getId()));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Map<String,String>> remove(@PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails ud) {
        favoriteService.removeFavorite(itemId, ((User)ud).getId());
        return ResponseEntity.ok(Map.of("message","Removed from favorites"));
    }
}
