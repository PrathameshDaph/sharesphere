package com.sharesphere.dto.response;
import com.sharesphere.entity.ItemStatus;
import com.sharesphere.entity.ListingType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
    private String condition;
    private BigDecimal price;
    private BigDecimal rentalPricePerDay;
    private BigDecimal securityDeposit;
    private int availableQuantity;
    private String location;
    private ListingType listingType;
    private ItemStatus status;
    private UserResponse owner;
    private List<String> imageUrls;
    private double averageRating;
    private int totalRatings;
    private int viewCount;
    private LocalDateTime createdAt;
    private boolean favorited;
}
