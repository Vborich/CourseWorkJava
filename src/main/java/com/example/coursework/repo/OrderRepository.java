package com.example.coursework.repo;

import com.example.coursework.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository  extends CrudRepository<Order, Long> {
}