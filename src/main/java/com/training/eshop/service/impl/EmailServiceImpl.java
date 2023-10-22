package com.training.eshop.service.impl;

import com.training.eshop.dao.UserDAO;
import com.training.eshop.model.User;
import com.training.eshop.service.EmailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:mail/email.properties")
@ComponentScan(value = "com/training/eshop")
public class EmailServiceImpl implements EmailService {

    @Value("${mail.username}")
    private String sendFrom;

    @Value("${permitted.url}")
    private String baseUrl;

    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final JavaMailSender emailSender;
    private final UserDAO userDAO;

    public EmailServiceImpl(SpringTemplateEngine thymeleafTemplateEngine, JavaMailSender emailSender, UserDAO userDAO) {
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailSender = emailSender;
        this.userDAO = userDAO;
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel,
                                                  String template) {
        Context thymeleafContext = new Context();

        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @SneakyThrows
    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(message);
    }

    @Override
    public void sendOrderDetailsMessage(Long orderId, String login) {
        User recipient = userDAO.getByLogin(login);

        Map<String, Object> templateModel = new HashMap<>();

        templateModel.put("orderId", orderId);
        templateModel.put("login", recipient.getLogin());
        templateModel.put("baseUrl", baseUrl);

        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Order details",
                templateModel, "orderDetails.html");
    }
}

