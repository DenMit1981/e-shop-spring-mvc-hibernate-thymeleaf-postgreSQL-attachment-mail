package com.training.eshop.converter;

import com.training.eshop.dto.OrderDto;
import com.training.eshop.model.Order;

import java.math.BigDecimal;

public interface OrderConverter {

    Order fromOrderDto(OrderDto orderDto, String login, BigDecimal totalPrice);
}
