package com.ayoam.cartservice.kafka.consumer;

import com.ayoam.cartservice.event.ProductStockChangedEvent;
import com.ayoam.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class CartConsumer {
    private CartService cartService;

    @Autowired
    public CartConsumer(CartService cartService) {
        this.cartService = cartService;
    }

    @KafkaListener(topics = "productUpdatedTopic")
    public void handleProductsStockUpdated(ProductStockChangedEvent event){
        cartService.updateCartItemsStock(event);
    }

    @KafkaListener(topics = "productDeletedTopic")
    public void handleProductsAvailabilityUpdated(ProductStockChangedEvent event){
        cartService.updateCartItemsAvailability(event);
    }
}
