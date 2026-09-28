package com.sharesphere.dto.response;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String icon;
    private String description;
    private long itemCount;
}
