package com.ayoam.orderservice.dto;

import com.ayoam.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersListResponse {
    private List<Order> orderList;
    private int totalCount=0;
    public OrdersListResponse(List<Order> orderList) {
        this.orderList = orderList;
    }
}
