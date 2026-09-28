package com.sharesphere.controller;

import com.sharesphere.dto.response.*;
import com.sharesphere.service.AdminService;
import com.sharesphere.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final ReportService reportService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users/{id}/block")
    public ResponseEntity<Map<String,String>> block(@PathVariable Long id) {
        adminService.blockUser(id);
        return ResponseEntity.ok(Map.of("message","User blocked"));
    }

    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<Map<String,String>> unblock(@PathVariable Long id) {
        adminService.unblockUser(id);
        return ResponseEntity.ok(Map.of("message","User unblocked"));
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> getItems() {
        return ResponseEntity.ok(adminService.getAllItems());
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Map<String,String>> removeItem(@PathVariable Long id) {
        adminService.removeItem(id);
        return ResponseEntity.ok(Map.of("message","Item removed"));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PostMapping("/reports/{id}/resolve")
    public ResponseEntity<ReportResponse> resolveReport(@PathVariable Long id,
            @RequestParam(required=false,defaultValue="") String note,
            @RequestParam(defaultValue="RESOLVED") String status) {
        return ResponseEntity.ok(reportService.resolveReport(id, note, status));
    }
}
