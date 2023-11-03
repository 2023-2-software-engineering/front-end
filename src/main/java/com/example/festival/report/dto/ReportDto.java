package com.example.festival.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long reportId;
    private String nickname; //작성자 닉네임
    private String address; //작성자 주소
    private String title;
    private String content;
    private Timestamp createdAt;
    private Boolean done; //조치 완?
}
