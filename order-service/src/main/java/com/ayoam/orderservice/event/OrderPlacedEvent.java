package com.ayoam.orderservice.event;

import com.ayoam.orderservice.dto.OrderRequest;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderPlacedEvent {
    private List<OrderRequest> orderRequestList;
}
