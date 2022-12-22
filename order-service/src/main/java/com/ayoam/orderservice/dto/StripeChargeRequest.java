package com.ayoam.orderservice.dto;

import lombok.Data;


@Data
public class StripeChargeRequest {
    private String id;
    private Float amount;
}
