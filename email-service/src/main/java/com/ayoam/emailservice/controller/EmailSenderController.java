package com.ayoam.emailservice.controller;

import com.ayoam.emailservice.model.Email;
import com.ayoam.emailservice.service.EmailSenderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailSenderController {
    private final EmailSenderService emailSenderService;

    @PostMapping("/send/html")
    public void sendHtmlMessage(@RequestBody Email email) throws MessagingException {
        emailSenderService.sendHtmlMessage(email);
    }

    @PostMapping("/send")
    public void sendSimpleMessage(@RequestBody Email email) {
        emailSenderService.sendSimpleMessage(email);
    }

    @PostMapping("/test")
    public void sendHtmlMessageTest() throws MessagingException {
        emailSenderService.sendHtmlMessageTest();
    }
}