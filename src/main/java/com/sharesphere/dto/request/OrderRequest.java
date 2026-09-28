package com.sharesphere.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull private Long itemId;
    private String buyerNote;
}
