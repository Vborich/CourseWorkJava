package com.example.coursework.services;

import com.example.coursework.models.Advertising;
import com.example.coursework.models.AdvertisingSubtype;
import com.example.coursework.models.Order;
import com.example.coursework.models.User;
import com.example.coursework.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdvertisingSubtypeRepository advertisingRepository;

    public boolean addOrder(int countUnits, long userId, long advertisingTypeId)
    {
        User user = userRepository.findById(userId).get();
        AdvertisingSubtype advertisingSubtype = advertisingRepository.findById(advertisingTypeId).get();

        if (advertisingSubtype.getPrice() * countUnits > 1000000)
            return false;

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setUser(user);
        order.setCompany(user.getCompany());
        order.setAdvertisingSubtype(advertisingSubtype);
        order.setCountUnits(countUnits);
        order.setTotalPrice(countUnits * advertisingSubtype.getPrice());
        order.setStatus(0);
        orderRepository.save(order);
        return true;
    }

    public boolean cancelOrder(long id)
    {
        Order order = orderRepository.findById(id).get();
        if (order.getStatus() != 0)
            return  false;
        order.setStatus(-1);
        orderRepository.save(order);
        return true;
    }

    public boolean acceptOrder(long id)
    {
        Order order = orderRepository.findById(id).get();
        if (order.getStatus() != 0)
            return  false;
        order.setStatus(1);
        order.setConfirmationDate(new Date());
        orderRepository.save(order);
        return true;
    }

    public Order getOrderById(long id)
    {
        return  orderRepository.findById(id).get();
    }

    public Iterable<Order> getOrders()
    {
        return orderRepository.findAll();
    }

}
