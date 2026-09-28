package com.sharesphere.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class MessageResponse {
    private Long id;
    private Long conversationId;
    private UserResponse sender;
    private String content;
    private boolean read;
    private LocalDateTime sentAt;
}
