package com.sharesphere.service;

import com.sharesphere.dto.request.ReviewRequest;
import com.sharesphere.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(ReviewRequest request, Long reviewerId);
    List<ReviewResponse> getReviewsForItem(Long itemId);
    List<ReviewResponse> getReviewsForUser(Long userId);
}
