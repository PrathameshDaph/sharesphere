package com.sharesphere.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ConversationResponse {
    private Long id;
    private UserResponse participant1;
    private UserResponse participant2;
    private MessageResponse lastMessage;
    private long unreadCount;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
}
