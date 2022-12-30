package com.ayoam.orderservice.controller;

import com.ayoam.orderservice.dto.OrdersListResponse;
import com.ayoam.orderservice.dto.OrderDto;
import com.ayoam.orderservice.dto.OrderStatusRequest;
import com.ayoam.orderservice.model.Order;
import com.ayoam.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<OrdersListResponse> getAllOrders(){
        return new ResponseEntity<OrdersListResponse>(orderService.getAllOrders(),HttpStatus.OK);
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

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping(value = "/invoice/{invoiceId}", produces = {MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<?> generateInvoice(@PathVariable Long invoiceId,HttpServletResponse response) throws JsonProcessingException, JRException, FileNotFoundException {
        return new ResponseEntity<>(orderService.generateInvoice(response,invoiceId),HttpStatus.OK);
    }
}
