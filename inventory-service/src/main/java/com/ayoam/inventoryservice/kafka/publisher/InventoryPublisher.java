package com.ayoam.inventoryservice.kafka.publisher;

import com.ayoam.inventoryservice.event.StockReservedEvent;
import com.ayoam.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InventoryPublisher {
    private KafkaTemplate<String, StockReservedEvent> kafkaTemplate;

    @Autowired
    public InventoryPublisher(KafkaTemplate<String, StockReservedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reserveStockEvent(StockReservedEvent stockReservedEvent){
        kafkaTemplate.send("productReserveStockTopic",stockReservedEvent);
    }
}
