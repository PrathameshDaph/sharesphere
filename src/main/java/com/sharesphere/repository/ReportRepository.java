package com.sharesphere.repository;

import com.sharesphere.entity.Report;
import com.sharesphere.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatusOrderByCreatedAtDesc(ReportStatus status);
    List<Report> findByReporterIdOrderByCreatedAtDesc(Long reporterId);
    long countByStatus(ReportStatus status);
}
