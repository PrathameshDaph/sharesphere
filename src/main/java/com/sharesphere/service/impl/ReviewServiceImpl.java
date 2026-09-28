package com.sharesphere.service.impl;

import com.sharesphere.dto.request.ReviewRequest;
import com.sharesphere.dto.response.ReviewResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.*;
import com.sharesphere.repository.*;
import com.sharesphere.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Override @Transactional
    public ReviewResponse createReview(ReviewRequest req, Long reviewerId) {
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Item item = req.getItemId() != null ? itemRepository.findById(req.getItemId()).orElse(null) : null;
        User reviewee = req.getRevieweeId() != null
                ? userRepository.findById(req.getRevieweeId()).orElse(
                    item != null ? item.getOwner() : null) : (item != null ? item.getOwner() : null);

        Review review = Review.builder()
                .reviewer(reviewer).reviewee(reviewee).item(item)
                .rating(req.getRating()).comment(req.getComment())
                .reviewType(req.getReviewType()).build();
        review = reviewRepository.save(review);

        // Update item average rating
        if (item != null) {
            Double avg = reviewRepository.findAverageRatingByItem(item.getId());
            long count = reviewRepository.findByItemIdOrderByCreatedAtDesc(item.getId()).size();
            item.setAverageRating(avg != null ? avg : 0.0);
            item.setTotalRatings((int) count);
            itemRepository.save(item);
        }
        // Update user average rating
        if (reviewee != null) {
            Double avg = reviewRepository.findAverageRatingByUser(reviewee.getId());
            long count = reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(reviewee.getId()).size();
            reviewee.setAverageRating(avg != null ? avg : 0.0);
            reviewee.setTotalRatings((int) count);
            userRepository.save(reviewee);
        }
        return toResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsForItem(Long itemId) {
        return reviewRepository.findByItemIdOrderByCreatedAtDesc(itemId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<ReviewResponse> getReviewsForUser(Long userId) {
        return reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    private ReviewResponse toResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .reviewer(r.getReviewer() != null ? userService.toResponse(r.getReviewer()) : null)
                .reviewee(r.getReviewee() != null ? userService.toResponse(r.getReviewee()) : null)
                .itemId(r.getItem() != null ? r.getItem().getId() : null)
                .itemName(r.getItem() != null ? r.getItem().getName() : null)
                .rating(r.getRating()).comment(r.getComment())
                .reviewType(r.getReviewType()).createdAt(r.getCreatedAt()).build();
    }
}
