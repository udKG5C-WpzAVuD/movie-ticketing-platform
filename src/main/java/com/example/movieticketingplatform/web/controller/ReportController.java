package com.example.movieticketingplatform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.movieticketingplatform.model.domain.ReportData;
import com.example.movieticketingplatform.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // 允许来自任何来源的请求
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<JSONObject> generateReport(
            @RequestParam String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ReportData reportData = reportService.generateReportData(reportType, startDate, endDate);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("data", reportData);
        if (!reportType.equals("daily")){
            jsonResult.put("chartData",reportService.generateChartData(reportType, startDate, endDate));
        }
        return ResponseEntity.ok(jsonResult);
    }



    @PostMapping("/report/download")
    public ResponseEntity<byte[]> downloadReportWithChart(
            @RequestParam String reportType,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestBody(required = false) Map<String, String> requestBody) throws IOException {

        String chartImage = requestBody != null ? requestBody.get("chartImage") : null;
        byte[] reportContent = reportService.generatePdfReport(reportType, startDate, endDate, chartImage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = String.format("运营报告_%s_%s至%s.pdf",
                reportType.equals("daily") ? "日报" :
                        reportType.equals("weekly") ? "周报" : "月报",
                startDate, endDate);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }
}
