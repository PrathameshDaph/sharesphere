package com.sharesphere.controller;

import com.sharesphere.dto.request.RentalRequest;
import com.sharesphere.dto.response.RentalResponse;
import com.sharesphere.entity.User;
import com.sharesphere.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalResponse> create(@Valid @RequestBody RentalRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.createRental(req, ((User)ud).getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getOne(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.getRentalById(id, ((User)ud).getId()));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RentalResponse>> myRentals(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.getMyRentals(((User)ud).getId()));
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<RentalResponse>> incoming(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.getIncomingRequests(((User)ud).getId()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<RentalResponse> accept(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.acceptRental(id, ((User)ud).getId()));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<RentalResponse> reject(@PathVariable Long id,
            @RequestParam(required=false) String note,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.rejectRental(id, ((User)ud).getId(), note));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<RentalResponse> activate(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.markAsActive(id, ((User)ud).getId()));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<RentalResponse> requestReturn(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.requestReturn(id, ((User)ud).getId()));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<RentalResponse> complete(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.completeRental(id, ((User)ud).getId()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<RentalResponse> cancel(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(rentalService.cancelRental(id, ((User)ud).getId()));
    }
}
