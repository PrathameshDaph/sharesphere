package com.sharesphere.service;

import com.sharesphere.dto.request.OrderRequest;
import com.sharesphere.dto.response.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request, Long buyerId);
    OrderResponse getOrderById(Long orderId, Long currentUserId);
    List<OrderResponse> getMyOrders(Long userId);
    OrderResponse confirmPayment(Long orderId, Long buyerId);
    List<OrderResponse> getAllOrders();
}
