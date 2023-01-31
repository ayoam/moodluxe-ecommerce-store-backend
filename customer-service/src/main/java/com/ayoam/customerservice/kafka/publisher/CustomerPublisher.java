package com.ayoam.customerservice.kafka.publisher;

import com.ayoam.customerservice.event.CustomerRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerPublisher {
    private KafkaTemplate<String, CustomerRegisteredEvent> kafkaTemplate;
    @Autowired
    public CustomerPublisher(KafkaTemplate<String, CustomerRegisteredEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendConfirmationEmailEvent(CustomerRegisteredEvent customerRegisteredEvent){
        kafkaTemplate.send("customerRegisteredTopic",customerRegisteredEvent);
    }
}
