package com.sharesphere.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportRequest {
    private Long reportedUserId;
    private Long reportedItemId;
    @NotBlank private String reason;
    private String description;
}
