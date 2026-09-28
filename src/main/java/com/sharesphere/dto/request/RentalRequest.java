package com.sharesphere.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RentalRequest {
    @NotNull private Long itemId;
    @NotNull private LocalDate startDate;
    @NotNull private LocalDate endDate;
    private String renterNote;
}
