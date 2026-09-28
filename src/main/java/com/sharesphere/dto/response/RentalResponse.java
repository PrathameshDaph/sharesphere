package com.sharesphere.dto.response;
import com.sharesphere.entity.RentalStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class RentalResponse {
    private Long id;
    private ItemResponse item;
    private UserResponse renter;
    private UserResponse owner;
    private LocalDate startDate;
    private LocalDate endDate;
    private long durationDays;
    private BigDecimal pricePerDay;
    private BigDecimal totalRentalPrice;
    private BigDecimal securityDeposit;
    private BigDecimal totalPayable;
    private RentalStatus status;
    private String renterNote;
    private String ownerNote;
    private LocalDateTime createdAt;
}
