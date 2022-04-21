package com.example.coursework.services;

import com.example.coursework.dto.ReportDemandDto;
import com.example.coursework.dto.ReportIncomeDto;
import com.example.coursework.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    OrderService orderService;

    @Autowired
    AdvertisingService advertisingService;

    public ArrayList<ReportIncomeDto> getReportIncomeData(Date startDate, Date endDate)
    {
        ArrayList<ReportIncomeDto> reportIncomeDtos = getDefaultReportIncomeData();
        ArrayList<Order> orders = (ArrayList<Order>) ((ArrayList<Order>) orderService.getOrders()).stream()
                .filter(order -> order.getStatus() == 1  && order.getConfirmationDate().after(startDate)
                        && order.getConfirmationDate().before(endDate)).collect(Collectors.toList());

        for (var order: orders
        ) {
            var reportIncome = reportIncomeDtos.stream().filter(demandAdvertisingDto ->
                    demandAdvertisingDto.getAdvertising().equals(order.getAdvertisingSubtype().getAdvertising()
                            .getAdvertisingName())).findFirst();
            if (reportIncome.isPresent()) {
                reportIncome.get().setCount(reportIncome.get().getCount() + 1);
                reportIncome.get().setTotalIncome(reportIncome.get().getTotalIncome() + order.getTotalPrice());
            }
        }
        return reportIncomeDtos;
    }

    private ArrayList<ReportIncomeDto> getDefaultReportIncomeData()
    {
        ArrayList<ReportIncomeDto> reportIncomeDtos = new ArrayList<>();
        for (var advertising: advertisingService.getAdvertisings()
        ) {
            reportIncomeDtos.add(new ReportIncomeDto(advertising.getAdvertisingName()));
        }
        return reportIncomeDtos;
    }

    public ArrayList<ReportDemandDto> getReportDemandData(int year, int quarter)
    {
        ArrayList<ReportDemandDto> reportDemandDtos = getDefaultReportDemandData();
        ArrayList<Order> orders = (ArrayList<Order>) ((ArrayList<Order>) orderService.getOrders()).stream()
                .filter(order -> order.getStatus() == 1  && (order.getConfirmationDate().getYear() + 1900 ) == year).collect(Collectors.toList());

        for (var order: orders
        ) {
            var reportDemandDto = reportDemandDtos.stream().filter(demandAdvertisingDto ->
                    demandAdvertisingDto.getAdvertising().equals(order.getAdvertisingSubtype().getAdvertising()
                            .getAdvertisingName())).findFirst();
            if (reportDemandDto.isPresent()) {
                var count = 0;
                var month = order.getConfirmationDate().getMonth();
                switch (quarter)
                {
                    case 1:
                        if(month >=0 && month < 3)
                            count++;
                        break;
                    case 2:
                        if(month >=3 && month < 6)
                            count++;
                        break;
                    case 3:
                        if(month >=6 && month < 9)
                            count++;
                        break;
                    case 4:
                        if(month >=9 && month < 12)
                            count++;
                        break;
                }
                reportDemandDto.get().getMonthsCounts().set(month%3,
                        reportDemandDto.get().getMonthsCounts().get(month%3) + count);
            }
        }
        return reportDemandDtos;
    }

    private ArrayList<ReportDemandDto> getDefaultReportDemandData()
    {
        ArrayList<ReportDemandDto> reportDemandDtos = new ArrayList<>();
        for (var advertising: advertisingService.getAdvertisings()
        ) {
            reportDemandDtos.add(new ReportDemandDto(advertising.getAdvertisingName()));
        }
        return reportDemandDtos;
    }
}
