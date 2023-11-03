package com.example.festival.report.service;

import com.example.festival.report.dto.ReportDto;
import com.example.festival.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(
            @Autowired ReportRepository reportRepository
    ) {
        this.reportRepository = reportRepository;
    }

    public void reportCreate(String identify, ReportDto reportDto) {
        this.reportRepository.reportCreate(identify, reportDto);
    }

    public ReportDto reportRead(Long reportId) {
        return this.reportRepository.reportRead(reportId);
    }

    public List<ReportDto> reportReadAll() {
        return this.reportRepository.reportReadAll();
    }

    public void reportUpdate(Long reportId, ReportDto reportDto) { //내용 변경
        this.reportRepository.reportUpdate(reportId, reportDto);
    }

    public void reportUpdateDone(Long reportId) { //조치완료 변경
        this.reportRepository.reportUpdateDone(reportId);
    }

    public void reportDelete(Long reportId) {
        this.reportRepository.reportDelete(reportId);
    }
}
