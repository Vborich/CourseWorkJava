package com.example.coursework.controllers;

import com.example.coursework.dto.DemandAdvertisingDto;
import com.example.coursework.dto.IncomeAdvertisingDto;
import com.example.coursework.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.ArrayList;

@Controller
@PreAuthorize("hasAuthority('Admin')")
public class ChartController {

    @Autowired
    ChartService chartService;

    @GetMapping("/demand-chart")
    public String viewDemand(Model model) {
        return "demand-chart";
    }

    @GetMapping("/demand-chart-get-values")
    public @ResponseBody ArrayList<DemandAdvertisingDto> getValues(String startDate, String endDate, Model model) {
        return chartService.getChartDemandValues(Date.valueOf(startDate), Date.valueOf(endDate));
    }

    @GetMapping("/income-chart")
    public String viewIncomeChart(Model model) {
        return "income-chart";
    }

    @GetMapping("/income-chart-get-values")
    public @ResponseBody ArrayList<IncomeAdvertisingDto> getValuesIncome(String year, Model model) {
        return chartService.getChartIncomeValues(Integer.valueOf(year));
    }
}
