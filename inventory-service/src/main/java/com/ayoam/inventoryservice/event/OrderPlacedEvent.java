package com.ayoam.inventoryservice.event;

import com.ayoam.inventoryservice.dto.OrderRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderPlacedEvent {
    private List<OrderRequest> orderRequestList;
}
