package com.sharesphere.service.impl;

import com.sharesphere.dto.request.OrderRequest;
import com.sharesphere.dto.response.OrderResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.*;
import com.sharesphere.repository.*;
import com.sharesphere.service.NotificationService;
import com.sharesphere.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Override @Transactional
    public OrderResponse createOrder(OrderRequest req, Long buyerId) {
        Item item = itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        if (item.getOwner().getId().equals(buyerId))
            throw new ValidationException("Cannot buy your own item");
        if (item.getStatus() != ItemStatus.AVAILABLE)
            throw new ValidationException("Item is not available");
        if (item.getListingType() == ListingType.RENT || item.getListingType() == ListingType.LEND)
            throw new ValidationException("This item is not for sale");
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = Order.builder()
                .item(item).buyer(buyer).seller(item.getOwner())
                .amount(item.getPrice()).buyerNote(req.getBuyerNote())
                .transactionRef("SS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();
        order = orderRepository.save(order);
        notificationService.sendNotification(item.getOwner().getId(),
                "Item Order Received", buyer.getName() + " wants to buy your " + item.getName(),
                NotificationType.ORDER_PLACED, order.getId(), "ORDER");
        return toResponse(order);
    }

    @Override
    public OrderResponse getOrderById(Long orderId, Long currentUserId) {
        Order order = findOrder(orderId);
        if (!order.getBuyer().getId().equals(currentUserId) && !order.getSeller().getId().equals(currentUserId))
            throw new UnauthorizedException("Not authorized");
        return toResponse(order);
    }

    @Override
    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> asBuyer = orderRepository.findByBuyerIdOrderByCreatedAtDesc(userId);
        List<Order> asSeller = orderRepository.findBySellerIdOrderByCreatedAtDesc(userId);
        return java.util.stream.Stream.concat(asBuyer.stream(), asSeller.stream())
                .distinct().sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse).toList();
    }

    @Override @Transactional
    public OrderResponse confirmPayment(Long orderId, Long buyerId) {
        Order order = findOrder(orderId);
        if (!order.getBuyer().getId().equals(buyerId)) throw new UnauthorizedException("Not your order");
        if (order.getPaymentStatus() != PaymentStatus.PENDING)
            throw new ValidationException("Payment already processed");
        order.setPaymentStatus(PaymentStatus.PAID);
        order.getItem().setStatus(ItemStatus.SOLD);
        itemRepository.save(order.getItem());
        order = orderRepository.save(order);
        notificationService.sendNotification(order.getSeller().getId(),
                "Item Sold!", order.getBuyer().getName() + " paid for " + order.getItem().getName(),
                NotificationType.ITEM_SOLD, order.getId(), "ORDER");
        return toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    public OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .item(itemService.toResponse(o.getItem(), null))
                .buyer(userService.toResponse(o.getBuyer()))
                .seller(userService.toResponse(o.getSeller()))
                .amount(o.getAmount()).paymentStatus(o.getPaymentStatus())
                .transactionRef(o.getTransactionRef()).buyerNote(o.getBuyerNote())
                .createdAt(o.getCreatedAt()).build();
    }
}
