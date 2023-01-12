package com.ayoam.orderservice.controller;

import com.ayoam.orderservice.dto.*;
import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDto orderDto){
        return new ResponseEntity<Order>(orderService.placeOrder(orderDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/updateStatus")
    public ResponseEntity<Order> updateOrderStatus(@RequestBody OrderStatusRequest orderStatus, @PathVariable Long id){
        return new ResponseEntity<Order>(orderService.updateOrderStatus(orderStatus,id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<OrdersListResponse> getOrdersByCustomerId(@PathVariable Long customerId){
        return new ResponseEntity<OrdersListResponse>(orderService.getOrdersByCustomerId(customerId),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return new ResponseEntity<Order>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<OrdersListResponse> getAllOrders(@RequestParam Map<String,String> filters){
        return new ResponseEntity<OrdersListResponse>(orderService.getAllOrders(filters),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/salesStatistics")
    public ResponseEntity<SalesStatisticsResponse> getSalesStatistics(){
        return new ResponseEntity<SalesStatisticsResponse>(orderService.getSalesStatistics(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/totalOrdersAndSales")
    public ResponseEntity<TotalOrdersAndSalesResponse> getTotalOrdersAndSales(){
        return new ResponseEntity<TotalOrdersAndSalesResponse>(orderService.getTotalOrdersAndSales(),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping(value = "/invoice/{invoiceId}", produces = {MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<?> generateInvoice(@PathVariable Long invoiceId, HttpServletResponse response) throws JsonProcessingException, FileNotFoundException {
        return new ResponseEntity<>(orderService.generateInvoice(response,invoiceId),HttpStatus.OK);
    }
}
