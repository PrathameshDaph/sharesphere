package com.sharesphere.service;

import com.sharesphere.dto.request.ReportRequest;
import com.sharesphere.dto.response.ReportResponse;
import java.util.List;

public interface ReportService {
    ReportResponse createReport(ReportRequest request, Long reporterId);
    List<ReportResponse> getAllReports();
    ReportResponse resolveReport(Long reportId, String adminNote, String status);
}
