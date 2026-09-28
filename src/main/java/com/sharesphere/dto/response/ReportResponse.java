package com.sharesphere.dto.response;
import com.sharesphere.entity.ReportStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ReportResponse {
    private Long id;
    private UserResponse reporter;
    private UserResponse reportedUser;
    private Long reportedItemId;
    private String reportedItemName;
    private String reason;
    private String description;
    private ReportStatus status;
    private String adminNote;
    private LocalDateTime createdAt;
}
