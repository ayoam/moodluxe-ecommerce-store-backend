package com.ayoam.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class SalesStatisticsResponse {
    private List<SalesStatistics> salesPerMonth;
}
