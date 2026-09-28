package com.sharesphere.service.impl;

import com.sharesphere.dto.request.RentalRequest;
import com.sharesphere.dto.response.RentalResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.*;
import com.sharesphere.repository.*;
import com.sharesphere.service.NotificationService;
import com.sharesphere.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    @Override
    public RentalResponse createRental(RentalRequest req, Long renterId) {
        Item item = itemRepository.findById(req.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        if (item.getOwner().getId().equals(renterId))
            throw new ValidationException("Cannot rent your own item");
        if (item.getStatus() != ItemStatus.AVAILABLE)
            throw new ValidationException("Item is not available for rent");
        if (item.getListingType() == ListingType.SELL)
            throw new ValidationException("This item is only for sale, not rental");
        if (req.getStartDate().isBefore(LocalDate.now()))
            throw new ValidationException("Start date cannot be in the past");
        if (!req.getEndDate().isAfter(req.getStartDate()))
            throw new ValidationException("End date must be after start date");

        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Rental rental = Rental.builder()
                .item(item).renter(renter).owner(item.getOwner())
                .startDate(req.getStartDate()).endDate(req.getEndDate())
                .pricePerDay(item.getRentalPricePerDay())
                .securityDeposit(item.getSecurityDeposit())
                .renterNote(req.getRenterNote()).build();
        rental.calculateTotals();
        rental = rentalRepository.save(rental);

        notificationService.sendNotification(item.getOwner().getId(),
                "New Rental Request",
                renter.getName() + " requested your " + item.getName() + " for " + rental.getDurationDays() + " days.",
                NotificationType.RENTAL_REQUEST, rental.getId(), "RENTAL");

        return toResponse(rental);
    }

    @Override
    public RentalResponse getRentalById(Long rentalId, Long currentUserId) {
        Rental rental = findRental(rentalId);
        if (!rental.getRenter().getId().equals(currentUserId) && !rental.getOwner().getId().equals(currentUserId))
            throw new UnauthorizedException("Not authorized to view this rental");
        return toResponse(rental);
    }

    @Override
    public List<RentalResponse> getMyRentals(Long userId) {
        return rentalRepository.findByRenterIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<RentalResponse> getIncomingRequests(Long ownerId) {
        return rentalRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public RentalResponse acceptRental(Long rentalId, Long ownerId) {
        Rental rental = findRental(rentalId);
        validateOwner(rental, ownerId);
        if (rental.getStatus() != RentalStatus.REQUESTED)
            throw new ValidationException("Can only accept REQUESTED rentals");
        rental.setStatus(RentalStatus.ACCEPTED);
        rental = rentalRepository.save(rental);
        notificationService.sendNotification(rental.getRenter().getId(),
                "Rental Accepted", "Your request for " + rental.getItem().getName() + " was accepted!",
                NotificationType.RENTAL_ACCEPTED, rental.getId(), "RENTAL");
        return toResponse(rental);
    }

    @Override @Transactional
    public RentalResponse rejectRental(Long rentalId, Long ownerId, String note) {
        Rental rental = findRental(rentalId);
        validateOwner(rental, ownerId);
        rental.setStatus(RentalStatus.REJECTED);
        rental.setOwnerNote(note);
        rental = rentalRepository.save(rental);
        notificationService.sendNotification(rental.getRenter().getId(),
                "Rental Rejected", "Your request for " + rental.getItem().getName() + " was rejected.",
                NotificationType.RENTAL_REJECTED, rental.getId(), "RENTAL");
        return toResponse(rental);
    }

    @Override @Transactional
    public RentalResponse markAsActive(Long rentalId, Long ownerId) {
        Rental rental = findRental(rentalId);
        validateOwner(rental, ownerId);
        if (rental.getStatus() != RentalStatus.ACCEPTED)
            throw new ValidationException("Rental must be ACCEPTED before marking ACTIVE");
        rental.setStatus(RentalStatus.ACTIVE);
        rental.getItem().setStatus(ItemStatus.RENTED);
        itemRepository.save(rental.getItem());
        rental = rentalRepository.save(rental);
        notificationService.sendNotification(rental.getRenter().getId(),
                "Rental Active", rental.getItem().getName() + " rental is now active. Enjoy!",
                NotificationType.RENTAL_ACTIVE, rental.getId(), "RENTAL");
        return toResponse(rental);
    }

    @Override @Transactional
    public RentalResponse requestReturn(Long rentalId, Long renterId) {
        Rental rental = findRental(rentalId);
        if (!rental.getRenter().getId().equals(renterId)) throw new UnauthorizedException("Not your rental");
        if (rental.getStatus() != RentalStatus.ACTIVE) throw new ValidationException("Rental is not ACTIVE");
        rental.setStatus(RentalStatus.RETURN_REQUESTED);
        rental = rentalRepository.save(rental);
        notificationService.sendNotification(rental.getOwner().getId(),
                "Return Requested", rental.getRenter().getName() + " wants to return " + rental.getItem().getName(),
                NotificationType.RENTAL_RETURN_REQUESTED, rental.getId(), "RENTAL");
        return toResponse(rental);
    }

    @Override @Transactional
    public RentalResponse completeRental(Long rentalId, Long ownerId) {
        Rental rental = findRental(rentalId);
        validateOwner(rental, ownerId);
        rental.setStatus(RentalStatus.COMPLETED);
        rental.getItem().setStatus(ItemStatus.AVAILABLE);
        itemRepository.save(rental.getItem());
        rental = rentalRepository.save(rental);
        notificationService.sendNotification(rental.getRenter().getId(),
                "Rental Completed", "Your rental of " + rental.getItem().getName() + " is complete.",
                NotificationType.RENTAL_COMPLETED, rental.getId(), "RENTAL");
        return toResponse(rental);
    }

    @Override @Transactional
    public RentalResponse cancelRental(Long rentalId, Long userId) {
        Rental rental = findRental(rentalId);
        if (!rental.getRenter().getId().equals(userId) && !rental.getOwner().getId().equals(userId))
            throw new UnauthorizedException("Not authorized");
        if (rental.getStatus() == RentalStatus.ACTIVE || rental.getStatus() == RentalStatus.COMPLETED)
            throw new ValidationException("Cannot cancel an active or completed rental");
        rental.setStatus(RentalStatus.CANCELLED);
        return toResponse(rentalRepository.save(rental));
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void checkOverdueRentals() {
        rentalRepository.findOverdueRentals(LocalDate.now()).forEach(rental -> {
            rental.setStatus(RentalStatus.OVERDUE);
            rentalRepository.save(rental);
            notificationService.sendNotification(rental.getOwner().getId(),
                    "Rental Overdue", rental.getItem().getName() + " rental is overdue!",
                    NotificationType.RENTAL_OVERDUE, rental.getId(), "RENTAL");
        });
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found: " + id));
    }

    private void validateOwner(Rental rental, Long ownerId) {
        if (!rental.getOwner().getId().equals(ownerId)) throw new UnauthorizedException("Not your rental");
    }

    public RentalResponse toResponse(Rental r) {
        return RentalResponse.builder()
                .id(r.getId())
                .item(itemService.toResponse(r.getItem(), null))
                .renter(userService.toResponse(r.getRenter()))
                .owner(userService.toResponse(r.getOwner()))
                .startDate(r.getStartDate()).endDate(r.getEndDate())
                .durationDays(r.getDurationDays())
                .pricePerDay(r.getPricePerDay())
                .totalRentalPrice(r.getTotalRentalPrice())
                .securityDeposit(r.getSecurityDeposit())
                .totalPayable(r.getTotalPayable())
                .status(r.getStatus())
                .renterNote(r.getRenterNote()).ownerNote(r.getOwnerNote())
                .createdAt(r.getCreatedAt()).build();
    }
}
