package com.ayoam.orderservice.kafka.publisher;

import com.ayoam.orderservice.event.OrderPlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderPublisher {
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Autowired
    public OrderPublisher(KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void placeOrderEvent(OrderPlacedEvent orderPlacedEvent){
        kafkaTemplate.send("orderPlacedTopic",orderPlacedEvent);
    }
}
