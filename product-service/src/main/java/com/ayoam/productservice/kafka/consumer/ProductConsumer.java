package com.ayoam.productservice.kafka.consumer;

import com.ayoam.productservice.event.StockReservedEvent;
import com.ayoam.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductConsumer {
    private ProductService productService;

    @Autowired
    public ProductConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "productReserveStockTopic")
    public void handleReserveStock(StockReservedEvent stockReservedEvent){
//        log.info("received notification : number of products "+stockReservedEvent.getProductsList().get(0).getProductId());
        productService.updateProductsStock(stockReservedEvent);
    }
}
