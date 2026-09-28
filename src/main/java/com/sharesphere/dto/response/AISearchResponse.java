package com.sharesphere.dto.response;
import com.sharesphere.entity.ListingType;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class AISearchResponse {
    private String originalQuery;
    private String detectedCategory;
    private ListingType detectedListingType;
    private Integer detectedDurationDays;
    private String detectedLocation;
    private String aiSuggestion;
    private List<ItemResponse> results;
}
