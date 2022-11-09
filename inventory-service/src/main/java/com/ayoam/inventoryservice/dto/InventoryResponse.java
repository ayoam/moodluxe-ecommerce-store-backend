package com.ayoam.inventoryservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryResponse {
    private Long productId;
    private Boolean inStock;
}
