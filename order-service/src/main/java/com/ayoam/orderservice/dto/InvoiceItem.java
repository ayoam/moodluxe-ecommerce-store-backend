package com.ayoam.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceItem {
    private String name;
    private int quantity;
    private Double unit_cost;
}
