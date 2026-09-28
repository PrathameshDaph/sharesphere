package com.sharesphere.service;

import com.sharesphere.dto.response.NotificationResponse;
import com.sharesphere.entity.NotificationType;
import java.util.List;

public interface NotificationService {
    void sendNotification(Long userId, String title, String message, NotificationType type, Long refId, String refType);
    List<NotificationResponse> getMyNotifications(Long userId);
    long getUnreadCount(Long userId);
    void markAllAsRead(Long userId);
    void markAsRead(Long notificationId, Long userId);
}
