package com.example.coursework.services;

import com.example.coursework.models.AdvertisingSubtype;
import com.example.coursework.models.Company;
import com.example.coursework.models.Order;
import com.example.coursework.models.User;
import com.example.coursework.repo.AdvertisingSubtypeRepository;
import com.example.coursework.repo.OrderRepository;
import com.example.coursework.repo.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AdvertisingSubtypeRepository advertisingRepository;

    @Test
    void cancelOrder() {
        Order order = new Order();
        order.setStatus(0);

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(1L);

        boolean isCanceled = orderService.cancelOrder(1);

        Assert.assertTrue(isCanceled);
        Assert.assertEquals(-1, order.getStatus());

        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
    }

    @Test
    void tryToCancelNotPendingOrder() {
        Order order = new Order();
        order.setStatus(1);

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(1L);

        boolean isCanceled = orderService.cancelOrder(1);

        Assert.assertFalse(isCanceled);
        Assert.assertNotEquals(-1, order.getStatus());

        Mockito.verify(orderRepository, Mockito.times(0)).save(order);
    }

    @Test
    void acceptOrder() {
        Order order = new Order();
        order.setStatus(0);

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(1L);

        boolean isAccepted = orderService.acceptOrder(1);

        Assert.assertTrue(isAccepted);
        Assert.assertEquals(1, order.getStatus());
        Assert.assertNotNull(order.getConfirmationDate());
        Assert.assertEquals(new Date().getDate(), order.getConfirmationDate().getDate());

        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
    }

    @Test
    void tryToAcceptCanceledOrder() {
        Order order = new Order();
        order.setStatus(-1);

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(1L);

        boolean isAccepted = orderService.acceptOrder(1);

        Assert.assertFalse(isAccepted);
        Assert.assertNotEquals(1, order.getStatus());
        Assert.assertNull(order.getConfirmationDate());

        Mockito.verify(orderRepository, Mockito.times(0)).save(order);
    }

    @Test
    void addOrder() {
        User user = new User();
        user.setCompany(new Company());

        AdvertisingSubtype advertisingSubtype = new AdvertisingSubtype();
        advertisingSubtype.setPrice(10);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(Optional.of(advertisingSubtype)).when(advertisingRepository).findById(1L);

        boolean isAdded = orderService.addOrder(10, 1, 1);

        Assert.assertTrue(isAdded);

        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void addOrderWithTooMuchTotalPrice() {
        User user = new User();
        user.setCompany(new Company());

        AdvertisingSubtype advertisingSubtype = new AdvertisingSubtype();
        advertisingSubtype.setPrice(10000000);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(Optional.of(advertisingSubtype)).when(advertisingRepository).findById(1L);

        boolean isAdded = orderService.addOrder(1000000, 1, 1);

        Assert.assertFalse(isAdded);

        Mockito.verify(orderRepository, Mockito.times(0)).save(Mockito.any());
    }
}