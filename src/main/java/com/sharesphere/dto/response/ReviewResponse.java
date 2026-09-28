package com.sharesphere.dto.response;
import com.sharesphere.entity.ReviewType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ReviewResponse {
    private Long id;
    private UserResponse reviewer;
    private UserResponse reviewee;
    private Long itemId;
    private String itemName;
    private int rating;
    private String comment;
    private ReviewType reviewType;
    private LocalDateTime createdAt;
}
