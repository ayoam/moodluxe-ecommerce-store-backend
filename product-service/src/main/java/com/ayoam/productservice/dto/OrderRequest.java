package com.ayoam.productservice.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequest {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
}