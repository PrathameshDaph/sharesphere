package com.sharesphere;

import com.sharesphere.dto.request.RentalRequest;
import com.sharesphere.entity.*;
import com.sharesphere.exception.ValidationException;
import com.sharesphere.repository.*;
import com.sharesphere.service.NotificationService;
import com.sharesphere.service.impl.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock RentalRepository rentalRepository;
    @Mock ItemRepository itemRepository;
    @Mock UserRepository userRepository;
    @Mock NotificationService notificationService;
    @Mock UserServiceImpl userService;
    @Mock ItemServiceImpl itemService;
    @InjectMocks RentalServiceImpl rentalService;

    private User makeUser(Long id) { return User.builder().id(id).name("User"+id).email("u"+id+"@t.com").build(); }
    private Item makeItem(Long id, Long ownerId) {
        return Item.builder().id(id).name("Item"+id).owner(makeUser(ownerId))
            .status(ItemStatus.AVAILABLE).listingType(ListingType.SELL_AND_RENT)
            .rentalPricePerDay(new BigDecimal("100")).securityDeposit(new BigDecimal("200")).build();
    }

    @Test
    void createRental_success() {
        Item item = makeItem(1L, 2L);
        User renter = makeUser(3L);
        RentalRequest req = new RentalRequest();
        req.setItemId(1L);
        req.setStartDate(LocalDate.now().plusDays(1));
        req.setEndDate(LocalDate.now().plusDays(4));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(3L)).thenReturn(Optional.of(renter));
        Rental saved = Rental.builder().id(1L).item(item).renter(renter).owner(item.getOwner())
            .startDate(req.getStartDate()).endDate(req.getEndDate())
            .pricePerDay(new BigDecimal("100")).securityDeposit(new BigDecimal("200"))
            .status(RentalStatus.REQUESTED).build();
        saved.calculateTotals();
        when(rentalRepository.save(any())).thenReturn(saved);
        when(itemService.toResponse(any(),any())).thenReturn(null);
        when(userService.toResponse(any())).thenReturn(null);

        var result = rentalService.createRental(req, 3L);
        assertNotNull(result);
        assertEquals(RentalStatus.REQUESTED, result.getStatus());
    }

    @Test
    void createRental_ownItem_throws() {
        Item item = makeItem(1L, 5L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        RentalRequest req = new RentalRequest();
        req.setItemId(1L);
        req.setStartDate(LocalDate.now().plusDays(1));
        req.setEndDate(LocalDate.now().plusDays(3));
        assertThrows(ValidationException.class, () -> rentalService.createRental(req, 5L));
    }
}
