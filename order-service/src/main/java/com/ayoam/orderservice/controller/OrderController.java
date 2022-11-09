package com.ayoam.orderservice.controller;

import com.ayoam.orderservice.dto.GetAllOrdersResponse;
import com.ayoam.orderservice.dto.OrderDto;
import com.ayoam.orderservice.dto.OrderStatusRequest;
import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.model.OrderStatus;
import com.ayoam.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDto orderDto){
        return new ResponseEntity<Order>(orderService.placeOrder(orderDto), HttpStatus.CREATED);
    }

    @PutMapping("/orders/{id}/updateStatus")
    public ResponseEntity<Order> updateOrderStatus(@RequestBody OrderStatusRequest orderStatus, @PathVariable Long id){
        return new ResponseEntity<Order>(orderService.updateOrderStatus(orderStatus,id), HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<GetAllOrdersResponse> getAllOrders(){
        return new ResponseEntity<GetAllOrdersResponse>(orderService.getAllOrders(),HttpStatus.OK);
    }
}
