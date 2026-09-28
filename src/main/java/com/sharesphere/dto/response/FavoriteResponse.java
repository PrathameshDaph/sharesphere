package com.sharesphere.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class FavoriteResponse {
    private Long id;
    private ItemResponse item;
    private LocalDateTime createdAt;
}
