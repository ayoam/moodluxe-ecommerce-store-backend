package com.ayoam.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CheckInventoryResponse {
    @JsonProperty("data")
    private List<InventoryResponse> inventoryDtoList;
}