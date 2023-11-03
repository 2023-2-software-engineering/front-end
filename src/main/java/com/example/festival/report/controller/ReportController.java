package com.example.festival.report.controller;

import com.example.festival.report.dto.ReportDto;
import com.example.festival.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(
            @Autowired ReportService reportService
    ) {
        this.reportService = reportService;
    }

    @PostMapping("")
    public void reportCreate(@RequestBody ReportDto reportDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.reportService.reportCreate(authentication.getName(), reportDto);
    }

    @GetMapping("/{reportId}")
    public ReportDto reportRead(@PathVariable("reportId")Long reportId) {
        return this.reportService.reportRead(reportId);
    }

    @GetMapping("")
    public List<ReportDto> reportReadAll() {
        return this.reportService.reportReadAll();
    }

    @PatchMapping("/{reportId}")
    public void reportUpdate(@PathVariable("reportId")Long reportId, @RequestBody ReportDto reportDto) {
        this.reportService.reportUpdate(reportId, reportDto);
    }

    @PatchMapping("/{reportId}/done")
    public void reportUpdateDone(@PathVariable("reportId")Long reportId) {
        this.reportService.reportUpdateDone(reportId);
    }

    @DeleteMapping("/{reportId}")
    public void reportDelete(@PathVariable("reportId")Long reportId) {
        this.reportService.reportDelete(reportId);
    }
}
