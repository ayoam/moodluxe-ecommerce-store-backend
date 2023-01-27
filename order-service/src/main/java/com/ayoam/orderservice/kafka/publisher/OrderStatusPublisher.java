package com.ayoam.orderservice.kafka.publisher;

import com.ayoam.orderservice.event.OrderPlacedEvent;
import com.ayoam.orderservice.event.OrderStatusChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusPublisher {
    private KafkaTemplate<String, OrderStatusChangedEvent> kafkaTemplate;

    @Autowired
    public OrderStatusPublisher(KafkaTemplate<String, OrderStatusChangedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStatusChangedEmailEvent(OrderStatusChangedEvent orderStatusChangedEvent){
        kafkaTemplate.send("orderStatusChangedTopic",orderStatusChangedEvent);
    }
}