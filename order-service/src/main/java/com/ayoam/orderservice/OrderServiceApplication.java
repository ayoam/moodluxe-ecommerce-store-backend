package com.ayoam.orderservice;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        Stripe.apiKey="sk_test_51LpDu1G29xxBejuG3ij6MrEizxCIZ4CteecHLrAoRxMQtCxxvJ0N4XlxX1XjzgRyhrZ4U6Q3Ryr7ILW3n29pJnRL0042hfyOnH";
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
