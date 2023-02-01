package com.ayoam.customerservice.kafka.publisher;

import com.ayoam.customerservice.dto.ForgotPasswordRequest;
import com.ayoam.customerservice.event.CustomerRegisteredEvent;
import com.ayoam.customerservice.event.ForgotPasswordEvent;
import com.ayoam.customerservice.model.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerPublisher {
    private KafkaTemplate<String, CustomerRegisteredEvent> kafkaTemplate1;
    private KafkaTemplate<String, ForgotPasswordEvent> kafkaTemplate2;
    @Autowired
    public CustomerPublisher(KafkaTemplate<String, CustomerRegisteredEvent> kafkaTemplate1,KafkaTemplate<String, ForgotPasswordEvent> kafkaTemplate2) {
        this.kafkaTemplate1 = kafkaTemplate1;
        this.kafkaTemplate2=kafkaTemplate2;
    }

    public void sendConfirmationEmailEvent(CustomerRegisteredEvent customerRegisteredEvent){
        kafkaTemplate1.send("customerRegisteredTopic",customerRegisteredEvent);
    }

    public void sendForgotPasswordEmailEvent(ForgotPasswordEvent forgotPasswordEvent){
        kafkaTemplate2.send("forgotPasswordTopic",forgotPasswordEvent);
    }
}
