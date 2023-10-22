package com.training.eshop.controller;

import com.training.eshop.dto.GoodDto;
import com.training.eshop.dto.OrderDto;
import com.training.eshop.model.Order;
import com.training.eshop.service.GoodService;
import com.training.eshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
public class GoodController {
    private static final Logger LOGGER = LogManager.getLogger(GoodController.class.getName());

    private static final String ORDER_DTO = "orderDto";
    private static final String CHOSEN_GOODS = "chosenGoods";

    private final GoodService goodService;
    private final OrderService orderService;

    @GetMapping("/goods")
    public String getAllGoods(Model model, HttpSession session, Authentication authentication) {
        List<GoodDto> goods = goodService.getAll();

        String login = authentication.getName();

        String options = goodService.getStringOfOptionsForDroppingMenuFromGoodList(goods);
        String chosenGoods = (String) session.getAttribute(CHOSEN_GOODS);
        String choice = goodService.getChoice(chosenGoods);

        model.addAttribute("login", login);
        model.addAttribute("options", options);
        model.addAttribute("choice", choice);

        return "goods";
    }

    @PostMapping("/createOrder")
    public String createOrder(HttpServletRequest request, HttpSession session, Authentication authentication) {
        OrderDto orderDto = orderService.getOrderDto(session);

        String command = request.getParameter("submit");

        return clickingActions(command, request, authentication, session, orderDto);
    }

    private String clickingActions(String command, HttpServletRequest request, Authentication authentication,
                                   HttpSession session, OrderDto orderDto) {
        session.setAttribute(ORDER_DTO, orderDto);

        BigDecimal totalPrice = orderService.getTotalPrice(orderDto);
        session.setAttribute("totalPrice", totalPrice);

        String option = goodService.getStringOfNameAndPriceFromOptionMenu(request.getParameter("goodName"));

        switch (command) {
            case "Add Goods":
                orderService.addGoodToOrder(option, orderDto);

                getChosenGoods(session, orderDto);

                return "redirect:/goods";
            case "Remove Goods":
                orderService.deleteGoodFromOrder(option, orderDto);

                getChosenGoods(session, orderDto);

                return "redirect:/goods";
            case "Submit":
                String orderList = orderService.printOrder(orderDto);

                session.setAttribute("orderList", orderList);

                Order order = orderService.save(orderDto, authentication.getName(), totalPrice);

                session.setAttribute("order", order);

                return "redirect:/order";
        }

        orderService.updateData(session, orderDto);

        return "redirect:/login";
    }

    private void getChosenGoods(HttpSession session, OrderDto orderDto) {
        String chosenGoods = orderService.printChosenGoods(orderDto);

        LOGGER.info("Chosen goods: {}", chosenGoods);

        session.setAttribute(CHOSEN_GOODS, chosenGoods);
    }
}
