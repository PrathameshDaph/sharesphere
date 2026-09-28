package com.sharesphere.service;

import com.sharesphere.dto.request.RentalRequest;
import com.sharesphere.dto.response.RentalResponse;
import java.util.List;

public interface RentalService {
    RentalResponse createRental(RentalRequest request, Long renterId);
    RentalResponse getRentalById(Long rentalId, Long currentUserId);
    List<RentalResponse> getMyRentals(Long userId);
    List<RentalResponse> getIncomingRequests(Long ownerId);
    RentalResponse acceptRental(Long rentalId, Long ownerId);
    RentalResponse rejectRental(Long rentalId, Long ownerId, String note);
    RentalResponse markAsActive(Long rentalId, Long ownerId);
    RentalResponse requestReturn(Long rentalId, Long renterId);
    RentalResponse completeRental(Long rentalId, Long ownerId);
    RentalResponse cancelRental(Long rentalId, Long userId);
}
