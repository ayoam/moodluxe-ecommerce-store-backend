package com.ayoam.productservice.event;

import com.ayoam.productservice.dto.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockReservedEvent {
    private List<OrderRequest> productsList;
}
