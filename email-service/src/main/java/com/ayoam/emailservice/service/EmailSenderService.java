package com.ayoam.emailservice.service;

import com.ayoam.emailservice.event.CustomerRegisteredEvent;
import com.ayoam.emailservice.event.ForgotPasswordEvent;
import com.ayoam.emailservice.event.OrderPlacedEvent;
import com.ayoam.emailservice.event.OrderStatusChangedEvent;
import com.ayoam.emailservice.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {
    @Value("${spring.mail.username}")
    private String emailUsername;

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendHtmlMessage(Email email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());
        helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process(email.getTemplate(), context);
        helper.setText(html, true);

        log.info("Sending email: {} with html body: {}", email, html);
        emailSender.send(message);
    }

    public void sendSimpleMessage(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getFrom());
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());

        log.info("Sending email: {} with text body: {}", email, email.getText());
        emailSender.send(message);
    }

    public void sendHtmlMessageTest() throws MessagingException {
        Email email = new Email();
        email.setTo("ayoub.amkhazzou.me@gmail.com");
        email.setFrom("Moodluxe Store <"+emailUsername+"@gmail.com>");
        email.setSubject("Welcome Email from Moodluxe");
//        email.setTemplate("test-email.html");
        email.setTemplate("order-shipped-template.html");
        Map<String, Object> properties = new HashMap<>();
        properties.put("orderNumber", "#"+"12548");
        properties.put("orderLink", "http://localhost:3000/orders");
//        properties.put("name", "Ashish");
//        properties.put("subscriptionDate", LocalDate.now().toString());
//        properties.put("technologies", Arrays.asList("Python", "Go", "C#"));
//        email.setTemplate("order-confirmation-template.html");
//        properties.put("name", "Elon");
//        properties.put("orderNumber", "#2599");
//        properties.put("orderDate", "25 December 2022");
//        properties.put("orderTotal", "$1099.99");
//        properties.put("orderLink", "http://localhost:3000/orders/111");
        email.setProperties(properties);
        sendHtmlMessage(email);
    }

    public void sendOrderConfirmationEmail(OrderPlacedEvent orderPlacedEvent) throws MessagingException {
        Email email = new Email();
        email.setTo(orderPlacedEvent.getCustomerEmail());
        email.setFrom("Moodluxe Store <"+emailUsername+"@gmail.com>");
        email.setSubject("Order Confirmation");
        email.setTemplate("order-confirmation-template.html");
        Map<String, Object> properties = new HashMap<>();

        properties.put("name", orderPlacedEvent.getCustomerName());
        Long orderNumber = orderPlacedEvent.getOrderNumber();
        properties.put("orderNumber", "#"+orderNumber);
        String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(orderPlacedEvent.getOrderDate());
        properties.put("orderDate", date);
        properties.put("orderTotal", "$"+orderPlacedEvent.getOrderTotal());
        properties.put("orderLink", "http://localhost:3000/orders/"+orderNumber);

        email.setProperties(properties);
        sendHtmlMessage(email);
    }


    public void sendStatusChangedEmail(OrderStatusChangedEvent orderStatusChangedEvent) throws MessagingException {
        Email email = new Email();
        Map<String, Object> properties = new HashMap<>();
        String orderStatus = orderStatusChangedEvent.getOrderStatus();
        Long orderNumber = orderStatusChangedEvent.getOrderNumber();
        email.setTo(orderStatusChangedEvent.getCustomerEmail());
        email.setFrom("Moodluxe Store <"+emailUsername+"@gmail.com>");

        if(Objects.equals(orderStatus, "Shipped")){
            email.setSubject("Order Shipped");
            email.setTemplate("order-shipped-template.html");
            properties.put("orderLink", "http://localhost:3000/orders/"+orderNumber);
        }
        else if(Objects.equals(orderStatus, "Delivered")){
            email.setSubject("Order Delivered");
            email.setTemplate("order-delivered-template.html");
            properties.put("allOrdersLink", "http://localhost:3000/myAccount");
        }
        else{
            return;
        }

        properties.put("orderNumber", "#"+orderNumber);

        email.setProperties(properties);
        sendHtmlMessage(email);
    }

    public void sendEmailConfirmation(CustomerRegisteredEvent customerRegisteredEvent) throws MessagingException {
        Email email = new Email();
        email.setTo(customerRegisteredEvent.getEmail());
        email.setFrom("Moodluxe Store <"+emailUsername+"@gmail.com>");
        email.setSubject("Email verification");
        email.setTemplate("email-confirmation-template.html");
        Map<String, Object> properties = new HashMap<>();

        properties.put("fullName", customerRegisteredEvent.getFullName());
        properties.put("verificationLink", "http://localhost:3000/confirm-account?token="+customerRegisteredEvent.getConfirmationToken());

        email.setProperties(properties);
        sendHtmlMessage(email);
    }

    public void sendPasswordResetEmail(ForgotPasswordEvent forgotPasswordEvent) throws MessagingException {
        Email email = new Email();
        email.setTo(forgotPasswordEvent.getEmail());
        email.setFrom("Moodluxe Store <"+emailUsername+"@gmail.com>");
        email.setSubject("Password reset");
        email.setTemplate("forgot-password-template.html");
        Map<String, Object> properties = new HashMap<>();

        properties.put("resetLink", "http://localhost:3000/password-reset?token="+forgotPasswordEvent.getResetPasswordToken());

        email.setProperties(properties);
        sendHtmlMessage(email);
    }
}