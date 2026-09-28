package com.sharesphere.service.impl;

import com.sharesphere.dto.response.NotificationResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.exception.UnauthorizedException;
import com.sharesphere.repository.NotificationRepository;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(Long userId, String title, String message,
                                  NotificationType type, Long refId, String refType) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;
        Notification n = Notification.builder()
                .user(user).title(title).message(message)
                .type(type).referenceId(refId).referenceType(refType)
                .build();
        notificationRepository.save(n);
    }

    @Override
    public List<NotificationResponse> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!n.getUser().getId().equals(userId)) throw new UnauthorizedException("Not your notification");
        n.setRead(true);
        notificationRepository.save(n);
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId()).title(n.getTitle()).message(n.getMessage())
                .type(n.getType()).read(n.isRead())
                .referenceId(n.getReferenceId()).referenceType(n.getReferenceType())
                .createdAt(n.getCreatedAt()).build();
    }
}
