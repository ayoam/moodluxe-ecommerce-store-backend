package com.ayoam.productservice;

import com.ayoam.productservice.event.StockReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

//    @KafkaListener(topics = "productReserveStockTopic")
//    public void handleReserveStock(StockReservedEvent stockReservedEvent){
//        //send out and email notification
////        log.info("received notification : number of products "+stockReservedEvent.getProductsList().get(0).getProductId());
//
//    }

}
