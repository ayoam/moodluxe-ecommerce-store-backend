package com.ayoam.emailservice.kafka.consumer;


import com.ayoam.emailservice.event.CustomerRegisteredEvent;
import com.ayoam.emailservice.event.ForgotPasswordEvent;
import com.ayoam.emailservice.event.OrderPlacedEvent;
import com.ayoam.emailservice.event.OrderStatusChangedEvent;
import com.ayoam.emailservice.service.EmailSenderService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class emailConsumer {
    private EmailSenderService emailSenderService;

    @Autowired
    public emailConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "orderPlacedTopic")
    public void handleSendOrderConfirmation(OrderPlacedEvent orderPlacedEvent) throws MessagingException {
//        log.info("received notification :  "+orderPlacedEvent.getOrderNumber());
        emailSenderService.sendOrderConfirmationEmail(orderPlacedEvent);
    }

    @KafkaListener(topics = "orderStatusChangedTopic")
    public void handleSendStatusChangedEmail(OrderStatusChangedEvent orderStatusChangedEvent) throws MessagingException {
//        log.info("received notification :  "+orderStatusChangedEvent.getOrderStatus());
        emailSenderService.sendStatusChangedEmail(orderStatusChangedEvent);
    }

    @KafkaListener(topics = "customerRegisteredTopic")
    public void handleSendEmailConfirmation(CustomerRegisteredEvent customerRegisteredEvent) throws MessagingException {
//        log.info("received notification :  "+customerRegisteredEvent.getConfirmationToken());
        emailSenderService.sendEmailConfirmation(customerRegisteredEvent);
    }

    @KafkaListener(topics = "forgotPasswordTopic")
    public void handleSendPasswordResetEmail(ForgotPasswordEvent forgotPasswordEvent) throws MessagingException {
//        log.info("received notification :  "+forgotPasswordEvent.getResetPasswordToken());
        emailSenderService.sendPasswordResetEmail(forgotPasswordEvent);
    }
}
