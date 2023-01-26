package com.ayoam.emailservice.service;

import com.ayoam.emailservice.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

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
        email.setFrom("Moodluxe Store <moodluxe.store@gmail.com>");
        email.setSubject("Welcome Email from Moodluxe");
        email.setTemplate("test-email.html");
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Ashish");
        properties.put("subscriptionDate", LocalDate.now().toString());
        properties.put("technologies", Arrays.asList("Python", "Go", "C#"));
//        email.setTemplate("order-confirmation.html");
//        properties.put("name", "Elon");
//        properties.put("orderNumber", "#2599");
//        properties.put("orderDate", "25 December 2022");
//        properties.put("orderTotal", "$1099.99");
//        properties.put("orderLink", "http://localhost:3000/orders/111");
        email.setProperties(properties);
        sendHtmlMessage(email);
    }

}