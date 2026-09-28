package com.sharesphere.dto.response;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AdminStatsResponse {
    private long totalUsers;
    private long totalItems;
    private long availableItems;
    private long activeRentals;
    private long completedRentals;
    private long totalOrders;
    private long paidOrders;
    private long pendingReports;
    private long blockedUsers;
}
