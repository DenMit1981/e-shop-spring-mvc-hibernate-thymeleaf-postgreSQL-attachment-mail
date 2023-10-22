package com.training.eshop.service;

import java.util.Map;

public interface EmailService {

    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel, String template);

    void sendOrderDetailsMessage(Long orderId, String login);
}
