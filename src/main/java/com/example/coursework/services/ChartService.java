package com.example.coursework.services;

import com.example.coursework.dto.DemandAdvertisingDto;
import com.example.coursework.dto.IncomeAdvertisingDto;
import com.example.coursework.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    OrderService orderService;

    @Autowired
    AdvertisingService advertisingService;

    public ArrayList<DemandAdvertisingDto> getChartDemandValues(Date startDate, Date endDate)
    {
        ArrayList<DemandAdvertisingDto> demandAdvertisings = getDefaultDemandAdvertising();
        ArrayList<Order> orders = (ArrayList<Order>) ((ArrayList<Order>) orderService.getOrders()).stream()
                .filter(order -> order.getStatus() == 1  && order.getConfirmationDate().after(startDate)
                        && order.getConfirmationDate().before(endDate)).collect(Collectors.toList());

        for (var order: orders
             ) {
            var demandAdvertising = demandAdvertisings.stream().filter(demandAdvertisingDto ->
                    demandAdvertisingDto.getAdvertising().equals(order.getAdvertisingSubtype().getAdvertising()
                            .getAdvertisingName())).findFirst();
            if (demandAdvertising.isPresent())
                demandAdvertising.get().setValue(demandAdvertising.get().getValue() + 1);
        }
        return demandAdvertisings;
    }

    private ArrayList<DemandAdvertisingDto> getDefaultDemandAdvertising()
    {
        ArrayList<DemandAdvertisingDto> demandAdvertising = new ArrayList<>();
        for (var advertising: advertisingService.getAdvertisings()
             ) {
            demandAdvertising.add(new DemandAdvertisingDto(advertising.getAdvertisingName(), 0));
        }
        return demandAdvertising;
    }

    public ArrayList<IncomeAdvertisingDto> getChartIncomeValues(int year)
    {
        ArrayList<IncomeAdvertisingDto> incomeAdvertisings = getDefaultIncomeAdvertising();
        ArrayList<Order> orders = (ArrayList<Order>) ((ArrayList<Order>) orderService.getOrders()).stream()
                .filter(order -> order.getStatus() == 1  && (order.getConfirmationDate().getYear() + 1900) == year)
                .collect(Collectors.toList());

        for (var order: orders
        ) {
            var incomeAdvertising = incomeAdvertisings.stream().filter(incomeAdvertisingDto ->
                    incomeAdvertisingDto.getAdvertising().equals(order.getAdvertisingSubtype().getAdvertising()
                            .getAdvertisingName())).findFirst();
            if (incomeAdvertising.isPresent())
                incomeAdvertising.get().getMonthsValues().set(order.getConfirmationDate().getMonth(),
                        incomeAdvertising.get().getMonthsValues().get(order.getConfirmationDate().getMonth()) +
                                order.getTotalPrice());
        }
        return incomeAdvertisings;
    }

    private ArrayList<IncomeAdvertisingDto> getDefaultIncomeAdvertising()
    {
        ArrayList<IncomeAdvertisingDto> incomeAdvertising = new ArrayList<>();
        for (var advertising: advertisingService.getAdvertisings()
        ) {
            incomeAdvertising.add(new IncomeAdvertisingDto(advertising.getAdvertisingName()));
        }
        return incomeAdvertising;
    }
}
