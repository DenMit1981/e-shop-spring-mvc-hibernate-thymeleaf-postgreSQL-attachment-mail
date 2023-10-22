package com.training.eshop.dao;

import com.training.eshop.model.Order;

import java.util.List;

public interface OrderDAO {

    void save(Order order);

    Order getById(Long id);

    List<Order> getAll();
}
