package com.ayoam.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InvoiceGenRequest {
    private String date_title;
    private String from;
    private String to;
    private String logo;
    private Long number;
    private String date;
    private double amount_paid;
    private List<InvoiceItem> items;
    private String notes;

}

