package com.ayoam.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalOrdersAndSalesResponse {
    private Long totalOrders;
    private Double totalSales;
}
