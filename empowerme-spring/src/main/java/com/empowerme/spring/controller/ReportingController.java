package com.empowerme.spring.controller;

import com.empowerme.spring.service.ReportingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportingService.generateMonthlyReport(userId));
    }

    @GetMapping("/{userId}/csv")
    public ResponseEntity<String> getReportCSV(@PathVariable Long userId) {
        String csvData = reportingService.exportToCSV(userId);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monthly_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}
