package com.sharesphere.controller;

import com.sharesphere.dto.request.ReportRequest;
import com.sharesphere.dto.response.ReportResponse;
import com.sharesphere.entity.User;
import com.sharesphere.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> create(@Valid @RequestBody ReportRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(reportService.createReport(req, ((User)ud).getId()));
    }
}
