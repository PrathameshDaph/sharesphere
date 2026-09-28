package com.sharesphere.dto.response;
import com.sharesphere.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class OrderResponse {
    private Long id;
    private ItemResponse item;
    private UserResponse buyer;
    private UserResponse seller;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String transactionRef;
    private String buyerNote;
    private LocalDateTime createdAt;
}
