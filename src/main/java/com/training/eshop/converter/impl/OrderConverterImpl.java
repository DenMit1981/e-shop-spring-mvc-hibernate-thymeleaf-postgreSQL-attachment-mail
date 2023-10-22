package com.training.eshop.converter.impl;

import com.training.eshop.converter.OrderConverter;
import com.training.eshop.dto.OrderDto;
import com.training.eshop.model.Order;
import com.training.eshop.model.User;
import com.training.eshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class OrderConverterImpl implements OrderConverter {

    private final UserService userService;

    @Override
    public Order fromOrderDto(OrderDto orderDto, String login, BigDecimal totalPrice) {
        User user = userService.getByLogin(login);

        orderDto.setUser(user);
        orderDto.setTotalPrice(totalPrice);

        Order order = new Order();

        order.setId(orderDto.getId());
        order.setUser(orderDto.getUser());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setGoods(orderDto.getGoods());
        order.setAttachments(orderDto.getAttachments());

        return order;
    }
}
