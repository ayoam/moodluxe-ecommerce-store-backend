package com.ayoam.orderservice.dto;

import com.ayoam.orderservice.model.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetAllOrdersResponse {
    private List<Order> orderList;
}
