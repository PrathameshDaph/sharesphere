package com.sharesphere.service.impl;

import com.sharesphere.dto.response.*;
import com.sharesphere.entity.*;
import com.sharesphere.repository.*;
import com.sharesphere.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RentalRepository rentalRepository;
    private final OrderRepository orderRepository;
    private final ReportRepository reportRepository;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Override
    public AdminStatsResponse getStats() {
        return AdminStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalItems(itemRepository.count())
                .availableItems(itemRepository.countByStatus(ItemStatus.AVAILABLE))
                .activeRentals(rentalRepository.countByStatus(RentalStatus.ACTIVE))
                .completedRentals(rentalRepository.countByStatus(RentalStatus.COMPLETED))
                .totalOrders(orderRepository.count())
                .paidOrders(orderRepository.countByPaymentStatus(PaymentStatus.PAID))
                .pendingReports(reportRepository.countByStatus(ReportStatus.PENDING))
                .blockedUsers(userRepository.countByBlocked(true))
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userService::toResponse).toList();
    }

    @Override
    public void blockUser(Long userId) { userService.blockUser(userId); }

    @Override
    public void unblockUser(Long userId) { userService.unblockUser(userId); }

    @Override
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream().map(i -> itemService.toResponse(i, null)).toList();
    }

    @Override
    public void removeItem(Long itemId) { itemService.adminDeleteItem(itemId); }
}
