package com.sharesphere.dto.request;

import com.sharesphere.entity.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemRequest {
    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotBlank(message = "Condition is required")
    private String condition;

    private BigDecimal price;
    private BigDecimal rentalPricePerDay;
    private BigDecimal securityDeposit;

    @Positive(message = "Quantity must be positive")
    private int availableQuantity = 1;

    private String location;

    @NotNull(message = "Listing type is required")
    private ListingType listingType;
}
