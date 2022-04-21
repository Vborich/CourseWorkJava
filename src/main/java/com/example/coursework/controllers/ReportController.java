package com.example.coursework.controllers;

import com.example.coursework.dto.ReportDemandDto;
import com.example.coursework.dto.ReportIncomeDto;
import com.example.coursework.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.ArrayList;

@PreAuthorize("hasAuthority('Admin')")
@Controller
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/income-report")
    public String viewIncomeReport(Model model) {
        return "income-report";
    }

    @GetMapping("/income-report-data")
    public @ResponseBody
    ArrayList<ReportIncomeDto> getValues(String startDate, String endDate, Model model) {
        return reportService.getReportIncomeData(Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @GetMapping("/demand-report")
    public String viewDemandReport(Model model) {
        return "demand-report";
    }

    @GetMapping("/demand-report-data")
    public @ResponseBody
    ArrayList<ReportDemandDto> getValuesForDemandReport(int year, int quarter, Model model) {
        return reportService.getReportDemandData(year, quarter);
    }
}
