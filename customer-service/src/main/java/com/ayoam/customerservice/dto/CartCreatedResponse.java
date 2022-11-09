package com.ayoam.customerservice.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartCreatedResponse {
    @NotNull
    private Long cartId;
}
