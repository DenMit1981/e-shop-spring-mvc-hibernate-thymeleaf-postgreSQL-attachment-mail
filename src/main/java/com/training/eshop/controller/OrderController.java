package com.training.eshop.controller;

import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.dto.OrderDto;
import com.training.eshop.model.Order;
import com.training.eshop.service.AttachmentService;
import com.training.eshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {

    private static final String ORDER_LIST = "orderList";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String NO_ORDER_ERROR = "noOrderError";
    private static final String FILE_WITHOUT_NAME_ERROR = "fileWithoutNameError";
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";

    private final OrderService orderService;
    private final AttachmentService attachmentService;

    @GetMapping("/order")
    public String showOrder(Model model, HttpSession session, Authentication authentication) {
        String login = authentication.getName();

        BigDecimal totalPrice = (BigDecimal) session.getAttribute(TOTAL_PRICE);
        String orderList = (String) session.getAttribute(ORDER_LIST);
        String orderHeader = orderService.getOrderHeader(totalPrice);

        Long orderId = getCurrentOrderId(session);

        List<AttachmentDto> files = attachmentService.getAllByOrderId(orderId);
        String fileHeader = attachmentService.showChosenFiles(files, orderId);

        String noOrderError = (String) session.getAttribute(NO_ORDER_ERROR);
        String fileWithoutNameError = (String) session.getAttribute(FILE_WITHOUT_NAME_ERROR);
        String fileUploadError = (String) session.getAttribute(FILE_UPLOAD_ERROR);

        model.addAttribute("login", login);
        model.addAttribute("orderHeader", orderHeader);
        model.addAttribute("fileHeader", fileHeader);
        model.addAttribute(ORDER_LIST, orderList);
        model.addAttribute(TOTAL_PRICE, totalPrice);
        model.addAttribute(FILE_UPLOAD_ERROR, fileUploadError);
        model.addAttribute(FILE_WITHOUT_NAME_ERROR, fileWithoutNameError);

        if (orderId != null) {
            model.addAttribute("files", files);
        } else {
            model.addAttribute(NO_ORDER_ERROR, noOrderError);
        }

        return "order";
    }

    @PostMapping("/upload")
    public String uploadAndDeleteFile(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                      OrderDto orderDto, HttpSession session) throws IOException {
        String command = request.getParameter("submit");

        return clickingActions(command, file, session, orderDto);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<AttachmentDto> downloadFileById(@PathVariable("id") Long attachmentId,
                                                          HttpServletResponse response,
                                                          HttpSession session) throws IOException {
        AttachmentDto attachmentDto = attachmentService.getById(attachmentId, getCurrentOrderId(session));

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + attachmentDto.getName();

        response.setHeader(headerKey, headerValue);

        try (ServletOutputStream outputStream = response.getOutputStream();) {
            outputStream.write(attachmentDto.getFile());
        }

        return ResponseEntity.ok(attachmentDto);
    }

    private String clickingActions(String command, @RequestParam("file") MultipartFile file,
                                   HttpSession session, OrderDto orderDto) throws IOException {
        Long orderId = getCurrentOrderId(session);

        AttachmentDto attachmentDto = attachmentService.getChosenAttachment(file, session, orderId);

        if (command.equals("Log out")) {
            orderService.updateData(session, orderDto);

            return "redirect:/login";
        } else {
            attachmentService.getEventsWithAttachment(attachmentDto, command, session, orderId);

            return "redirect:/order";
        }
    }

    private Long getCurrentOrderId(HttpSession session) {
        Order order = (Order) session.getAttribute("order");

        return order.getId();
    }
}
