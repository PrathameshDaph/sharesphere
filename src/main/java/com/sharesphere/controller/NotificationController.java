package com.sharesphere.controller;

import com.sharesphere.dto.response.NotificationResponse;
import com.sharesphere.entity.User;
import com.sharesphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAll(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(notificationService.getMyNotifications(((User)ud).getId()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String,Long>> unreadCount(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(((User)ud).getId())));
    }

    @PostMapping("/read-all")
    public ResponseEntity<Map<String,String>> readAll(@AuthenticationPrincipal UserDetails ud) {
        notificationService.markAllAsRead(((User)ud).getId());
        return ResponseEntity.ok(Map.of("message","All marked as read"));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Map<String,String>> readOne(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        notificationService.markAsRead(id, ((User)ud).getId());
        return ResponseEntity.ok(Map.of("message","Marked as read"));
    }
}
