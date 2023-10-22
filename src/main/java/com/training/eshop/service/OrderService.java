package com.training.eshop.service;

import com.training.eshop.dto.OrderDto;
import com.training.eshop.model.Order;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    Order save(OrderDto orderDto, String login, BigDecimal totalPrice);

    Order getById(Long id);

    List<Order> getAll();

    void addGoodToOrder(String option, OrderDto orderDto);

    void deleteGoodFromOrder(String option, OrderDto orderDto);

    String printChosenGoods(OrderDto orderDto);

    String printOrder(OrderDto orderDto);

    BigDecimal getTotalPrice(OrderDto orderDto);

    OrderDto getOrderDto(HttpSession session);

    String getOrderHeader(BigDecimal totalPrice);

    void updateData(HttpSession session, OrderDto order);
}

