package com.sharesphere.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.sharesphere.entity.ReviewType;

@Data
public class ReviewRequest {
    @NotNull private Long itemId;
    private Long revieweeId;
    @Min(1) @Max(5) private int rating;
    private String comment;
    private ReviewType reviewType = ReviewType.ITEM;
}
