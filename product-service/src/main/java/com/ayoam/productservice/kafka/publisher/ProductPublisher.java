package com.ayoam.productservice.kafka.publisher;

import com.ayoam.productservice.event.ProductStockChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductPublisher {
    private KafkaTemplate<String, ProductStockChangedEvent> kafkaTemplate;

    @Autowired
    public ProductPublisher(KafkaTemplate<String, ProductStockChangedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createProductEvent(ProductStockChangedEvent productStockChangedEvent){
        kafkaTemplate.send("productCreatedTopic",productStockChangedEvent);
    }

    public void updateProductEvent(ProductStockChangedEvent productStockChangedEvent){
        kafkaTemplate.send("productUpdatedTopic",productStockChangedEvent);
    }

    public void deleteProductEvent(ProductStockChangedEvent productStockChangedEvent){
        kafkaTemplate.send("productDeletedTopic",productStockChangedEvent);
    }
}
