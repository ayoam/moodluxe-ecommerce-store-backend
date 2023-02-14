package com.ayoam.inventoryservice.kafka.consumer;

import com.ayoam.inventoryservice.event.OrderPlacedEvent;
import com.ayoam.inventoryservice.event.ProductStockChangedEvent;
import com.ayoam.inventoryservice.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryConsumer {
    private InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "productCreatedTopic")
    public void handleProductCreated(ProductStockChangedEvent productStockChangedEvent){
        inventoryService.addProductToInventory(productStockChangedEvent);
    }

    @KafkaListener(topics = "productUpdatedTopic")
    public void handleProductUpdated(ProductStockChangedEvent productStockChangedEvent){
        inventoryService.updateInventory(productStockChangedEvent);
    }

    @KafkaListener(topics = "productDeletedTopic")
    public void handleProductDeleted(ProductStockChangedEvent productStockChangedEvent){
        inventoryService.deleteInventory(productStockChangedEvent);
    }

    @KafkaListener(topics="orderPlacedTopic")
    public void handleOrderPlaced(OrderPlacedEvent orderPlacedEvent){
        inventoryService.reserveStock(orderPlacedEvent.getOrderItemsList());
//        System.out.println(orderPlacedEvent.getOrderRequestList().get(0).getProductId());
//        log.info(orderPlacedEvent.getOrderRequestList().get(0).getProductId()+"");
    }
}
