package com.sharesphere.repository;

import com.sharesphere.entity.Order;
import com.sharesphere.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
    List<Order> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
    List<Order> findByItemId(Long itemId);
    long countByPaymentStatus(PaymentStatus status);
}
