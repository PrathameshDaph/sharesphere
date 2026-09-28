package com.sharesphere.controller;

import com.sharesphere.dto.request.ReviewRequest;
import com.sharesphere.dto.response.ReviewResponse;
import com.sharesphere.entity.User;
import com.sharesphere.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(reviewService.createReview(req, ((User)ud).getId()));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ReviewResponse>> byItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(reviewService.getReviewsForItem(itemId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(userId));
    }
}
