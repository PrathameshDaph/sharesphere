package com.sharesphere.service.impl;

import com.sharesphere.dto.request.ReportRequest;
import com.sharesphere.dto.response.ReportResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.repository.*;
import com.sharesphere.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;

    @Override
    public ReportResponse createReport(ReportRequest req, Long reporterId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User reportedUser = req.getReportedUserId() != null
                ? userRepository.findById(req.getReportedUserId()).orElse(null) : null;
        Item reportedItem = req.getReportedItemId() != null
                ? itemRepository.findById(req.getReportedItemId()).orElse(null) : null;
        Report report = Report.builder()
                .reporter(reporter).reportedUser(reportedUser).reportedItem(reportedItem)
                .reason(req.getReason()).description(req.getDescription()).build();
        return toResponse(reportRepository.save(report));
    }

    @Override
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ReportResponse resolveReport(Long reportId, String adminNote, String status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        report.setAdminNote(adminNote);
        report.setStatus(ReportStatus.valueOf(status.toUpperCase()));
        report.setResolvedAt(LocalDateTime.now());
        return toResponse(reportRepository.save(report));
    }

    private ReportResponse toResponse(Report r) {
        return ReportResponse.builder()
                .id(r.getId())
                .reporter(r.getReporter() != null ? userService.toResponse(r.getReporter()) : null)
                .reportedUser(r.getReportedUser() != null ? userService.toResponse(r.getReportedUser()) : null)
                .reportedItemId(r.getReportedItem() != null ? r.getReportedItem().getId() : null)
                .reportedItemName(r.getReportedItem() != null ? r.getReportedItem().getName() : null)
                .reason(r.getReason()).description(r.getDescription())
                .status(r.getStatus()).adminNote(r.getAdminNote())
                .createdAt(r.getCreatedAt()).build();
    }
}
