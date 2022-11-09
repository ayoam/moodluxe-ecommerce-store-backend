package com.ayoam.orderservice.service;

import com.ayoam.orderservice.converter.OrderConverter;
import com.ayoam.orderservice.dto.*;
import com.ayoam.orderservice.event.OrderPlacedEvent;
import com.ayoam.orderservice.kafka.publisher.OrderPublisher;
import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.model.OrderLineItem;
import com.ayoam.orderservice.model.OrderStatus;
import com.ayoam.orderservice.repository.OrderRepository;
import com.ayoam.orderservice.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OrderConverter orderConverter;
    private OrderStatusRepository orderStatusRepository;
    private WebClient.Builder webClientBuiler;
    private OrderPublisher orderPublisher;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter, OrderStatusRepository orderStatusRepository, WebClient.Builder webClientBuiler, OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.orderStatusRepository = orderStatusRepository;
        this.webClientBuiler = webClientBuiler;
        this.orderPublisher = orderPublisher;
    }

    public Order placeOrder(OrderDto orderDto) {
        Order order = orderConverter.orderDtoToOrder(orderDto);
        OrderStatus status = orderStatusRepository.findById(orderDto.getStatusId()).orElse(null);
        if(status==null){
            throw new RuntimeException("Order status not found!");
        }
        order.setStatus(status);

        //orderRequest list
        List<OrderRequest> orderRequestList = new ArrayList<>();
        for(OrderLineItem item:orderDto.getOrderLineItemList()){
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setProductId(item.getProductId());
            orderRequest.setQuantity(item.getQuantity());
            orderRequestList.add(orderRequest);
        }

        //check inventory in inventory service ==> WebClient
        InventoryResponse[] result = new InventoryResponse[0];
        result = webClientBuiler.build().post()
                .uri("http://localhost:8080/api/v1/inventories/checkInventory")
                .body(Mono.just(orderRequestList),new ParameterizedTypeReference<List<OrderRequest>>() {})
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        Boolean allItemsAvailable = (Arrays.stream(result).allMatch(InventoryResponse::getInStock));
        if(!allItemsAvailable){
            throw new RuntimeException("some items are not available at the moment!");
        }
        //make payment ==> stripe API / paypal API

        //send order placed event to inventory service ==> Kafka
        OrderPlacedEvent event= new OrderPlacedEvent();
        event.setOrderRequestList(orderRequestList);
        orderPublisher.placeOrderEvent(event);

        return null;
//        return orderRepository.save(order);
    }

    public Order updateOrderStatus(OrderStatusRequest orderStatus, Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order==null){
            throw new RuntimeException("Order not found!");
        }
        OrderStatus status = orderStatusRepository.findById(orderStatus.getOrderStatusId()).orElse(null);
        if(status==null){
            throw new RuntimeException("Order status not found!");
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public GetAllOrdersResponse getAllOrders() {
        GetAllOrdersResponse res = new GetAllOrdersResponse();
        res.setOrderList(orderRepository.findAll());
        return res;
    }
}
