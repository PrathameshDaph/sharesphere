package com.sharesphere;

import com.sharesphere.ai.RuleBasedAIService;
import com.sharesphere.dto.response.AISearchResponse;
import com.sharesphere.repository.*;
import com.sharesphere.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AIServiceTest {

    @Mock ItemRepository itemRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock SearchHistoryRepository searchHistoryRepository;
    @Mock FavoriteRepository favoriteRepository;
    @Mock ItemServiceImpl itemService;
    @InjectMocks RuleBasedAIService aiService;

    @Test
    void parseAndSearch_detectsCalculatorCategory() {
        when(categoryRepository.findAll()).thenReturn(List.of(
            com.sharesphere.entity.Category.builder().id(3L).name("Calculators").icon("🧮").build()
        ));
        when(itemRepository.searchItems(any(),any(),any(),any(),any(),any(),any(),any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of()));

        AISearchResponse res = aiService.parseAndSearch("I need a calculator for my exam", null);

        assertNotNull(res);
        assertEquals("Calculators", res.getDetectedCategory());
        assertNotNull(res.getAiSuggestion());
    }

    @Test
    void parseAndSearch_detectsRentForDays() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        when(itemRepository.searchItems(any(),any(),any(),any(),any(),any(),any(),any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of()));

        AISearchResponse res = aiService.parseAndSearch("camera for 3 days", null);

        assertEquals(com.sharesphere.entity.ListingType.RENT, res.getDetectedListingType());
        assertEquals(3, res.getDetectedDurationDays());
    }

    @Test
    void parseAndSearch_detectsLocation() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        when(itemRepository.searchItems(any(),any(),any(),any(),any(),any(),any(),any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of()));

        AISearchResponse res = aiService.parseAndSearch("cycle near hostel", null);
        assertEquals("hostel", res.getDetectedLocation());
    }
}
