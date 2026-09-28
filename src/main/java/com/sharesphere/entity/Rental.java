package com.sharesphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private BigDecimal pricePerDay;
    private BigDecimal totalRentalPrice;
    private BigDecimal securityDeposit;
    private BigDecimal totalPayable;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RentalStatus status = RentalStatus.REQUESTED;

    private String renterNote;
    private String ownerNote;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public long getDurationDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public void calculateTotals() {
        long days = getDurationDays();
        if (pricePerDay != null && days > 0) {
            totalRentalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));
            totalPayable = totalRentalPrice.add(
                    securityDeposit != null ? securityDeposit : BigDecimal.ZERO);
        }
    }
}
