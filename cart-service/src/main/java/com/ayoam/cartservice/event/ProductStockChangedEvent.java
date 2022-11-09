package com.ayoam.cartservice.event;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductStockChangedEvent {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
}
