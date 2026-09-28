package com.sharesphere.controller;

import com.sharesphere.ai.AIRecommendationService;
import com.sharesphere.dto.response.AISearchResponse;
import com.sharesphere.dto.response.ItemResponse;
import com.sharesphere.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final AIRecommendationService aiService;

    @GetMapping("/search")
    public ResponseEntity<AISearchResponse> search(@RequestParam String query,
            @AuthenticationPrincipal UserDetails ud) {
        Long userId = ud != null ? ((User)ud).getId() : null;
        return ResponseEntity.ok(aiService.parseAndSearch(query, userId));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<ItemResponse>> recommendations(@AuthenticationPrincipal UserDetails ud) {
        Long userId = ud != null ? ((User)ud).getId() : null;
        return ResponseEntity.ok(aiService.getPersonalizedRecommendations(userId));
    }

    @GetMapping("/similar/{itemId}")
    public ResponseEntity<List<ItemResponse>> similar(@PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails ud) {
        Long userId = ud != null ? ((User)ud).getId() : null;
        return ResponseEntity.ok(aiService.getSimilarItems(itemId, userId));
    }
}
