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
class ChartServiceTest {

    @Autowired
    ChartService chartService;

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
    void getChartDemandValues() {
        Calendar calendar = new GregorianCalendar(2022, 1 , 1);
        Date startDate = calendar.getTime();

        calendar = new GregorianCalendar(2022, 4 , 1);
        Date endDate = calendar.getTime();

        var chartValues = chartService.getChartDemandValues(startDate, endDate);

        Assert.assertEquals(advertisings.size(), chartValues.size());

        Assert.assertEquals(advertisings.get(0).getAdvertisingName(), chartValues.get(0).getAdvertising());
        Assert.assertEquals(0, chartValues.get(0).getValue());

        Assert.assertEquals(advertisings.get(1).getAdvertisingName(), chartValues.get(1).getAdvertising());
        Assert.assertEquals(orders.size(), chartValues.get(1).getValue());
    }

    @Test
    void getChartDemandValuesWithBadSpacingOfOrders() {
        var chartValues = chartService.getChartDemandValues(new Date(), new Date());

        Assert.assertEquals(advertisings.size(), chartValues.size());
        for (int i = 0; i < advertisings.size(); i++) {
            Assert.assertEquals(advertisings.get(i).getAdvertisingName(), chartValues.get(i).getAdvertising());
            Assert.assertEquals(0, chartValues.get(i).getValue());
        }
    }

    @Test
    void getChartIncomeValues() {
        var chartValues = chartService.getChartIncomeValues(2022);

        Assert.assertEquals(advertisings.size(), chartValues.size());

        Assert.assertEquals(advertisings.get(0).getAdvertisingName(), chartValues.get(0).getAdvertising());
        Assert.assertArrayEquals(new ArrayList<Double>(List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
                        .toArray(), chartValues.get(0).getMonthsValues().toArray());

        Assert.assertEquals(advertisings.get(1).getAdvertisingName(), chartValues.get(1).getAdvertising());
        Assert.assertArrayEquals(new ArrayList<Double>(List.of(0.0, 0.0, 500.0, 600.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
                .toArray(), chartValues.get(1).getMonthsValues().toArray());
    }

    @Test
    void getChartIncomeValuesWithYearWithoutOrders() {
        var chartValues = chartService.getChartIncomeValues(1999);

        Assert.assertEquals(advertisings.size(), chartValues.size());
        for (int i = 0; i < advertisings.size(); i++) {
            Assert.assertEquals(advertisings.get(i).getAdvertisingName(), chartValues.get(i).getAdvertising());
            Assert.assertArrayEquals(new ArrayList<Double>(List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
                    .toArray(), chartValues.get(i).getMonthsValues().toArray());
        }
    }
}