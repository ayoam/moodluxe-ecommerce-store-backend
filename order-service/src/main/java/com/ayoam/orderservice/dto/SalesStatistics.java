package com.ayoam.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesStatistics {
    private int month;
    private Double total;
}
