package com.example.coursework.services;

import com.example.coursework.models.Advertising;
import com.example.coursework.models.AdvertisingSubtype;
import com.example.coursework.models.Order;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @MockBean
    OrderService orderService;

    @MockBean
    AdvertisingService advertisingService;

    private ArrayList<Advertising> advertisings;

    private ArrayList<Order> orders;

    @BeforeEach
    public void initDataForTests() {
        advertisings = new ArrayList<>();
        advertisings.add(new Advertising("vlad", "vwefwef", Collections.singleton(
                new AdvertisingSubtype())));
        advertisings.add(new Advertising("vlad1", "fewfwefwefwef",
                Collections.singleton(new AdvertisingSubtype())));

        ArrayList<AdvertisingSubtype> advertisingSubtypes = new ArrayList<>();
        advertisingSubtypes.add(new AdvertisingSubtype("vsvreve", "vewrwef",
                10, advertisings.get(1)));
        advertisingSubtypes.add(new AdvertisingSubtype("vsvr11eve", "vewrwef",
                20, advertisings.get(1)));

        orders = new ArrayList<>();
        orders.add(new Order(1, new GregorianCalendar(2022, 2 , 1).getTime(), 500,
                advertisingSubtypes.get(0)));
        orders.add(new Order(1, new GregorianCalendar(2022, 3 , 1).getTime(), 600,
                advertisingSubtypes.get(1)));

        Mockito.doReturn(advertisings).when(advertisingService).getAdvertisings();
        Mockito.doReturn(orders).when(orderService).getOrders();
    }

    @Test
    void getReportIncomeData() {
        Calendar calendar = new GregorianCalendar(2022, 1 , 1);
        Date startDate = calendar.getTime();

        calendar = new GregorianCalendar(2022, 4 , 1);
        Date endDate = calendar.getTime();

        var reportValues = reportService.getReportIncomeData(startDate, endDate);

        Assert.assertEquals(advertisings.size(), reportValues.size());

        Assert.assertEquals(advertisings.get(0).getAdvertisingName(), reportValues.get(0).getAdvertising());
        assertEquals(0.0, reportValues.get(0).getTotalIncome());
        Assert.assertEquals(0, reportValues.get(0).getCount());

        Assert.assertEquals(advertisings.get(1).getAdvertisingName(), reportValues.get(1).getAdvertising());
        assertEquals(orders.get(0).getTotalPrice() + orders.get(1).getTotalPrice(),
                reportValues.get(1).getTotalIncome());
        Assert.assertEquals(orders.size(), reportValues.get(1).getCount());
    }

    @Test
    void getReportIncomeDataWithBadSpacingOfOrders() {
        var reportValues = reportService.getReportIncomeData(new Date(), new Date());

        Assert.assertEquals(advertisings.size(), reportValues.size());
        for (int i = 0; i < advertisings.size(); i++) {
            Assert.assertEquals(advertisings.get(i).getAdvertisingName(), reportValues.get(i).getAdvertising());
            assertEquals(0.0, reportValues.get(i).getTotalIncome());
            Assert.assertEquals(0, reportValues.get(i).getCount());
        }
    }

    @Test
    void getReportDemandData() {
        var reportValues = reportService.getReportDemandData(2022, 2);

        Assert.assertEquals(advertisings.size(), reportValues.size());

        Assert.assertEquals(advertisings.get(0).getAdvertisingName(), reportValues.get(0).getAdvertising());
        Assert.assertArrayEquals(new ArrayList<Integer>(List.of(0,0,0)).toArray(),
                reportValues.get(0).getMonthsCounts().toArray());

        Assert.assertEquals(advertisings.get(1).getAdvertisingName(), reportValues.get(1).getAdvertising());
        Assert.assertArrayEquals(new ArrayList<Integer>(List.of(1,0,0)).toArray(),
                reportValues.get(1).getMonthsCounts().toArray());
    }

    @Test
    void getReportDemandDataWithYearWithoutOrders() {
        var reportValues = reportService.getReportDemandData(1999, 2);

        Assert.assertEquals(advertisings.size(), reportValues.size());
        for (int i = 0; i < advertisings.size(); i++)
        {
            Assert.assertEquals(advertisings.get(i).getAdvertisingName(), reportValues.get(i).getAdvertising());
            Assert.assertArrayEquals(new ArrayList<Integer>(List.of(0,0,0)).toArray(),
                    reportValues.get(i).getMonthsCounts().toArray());
        }
    }
}