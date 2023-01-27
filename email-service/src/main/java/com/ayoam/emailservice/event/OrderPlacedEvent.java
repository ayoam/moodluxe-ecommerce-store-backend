package com.ayoam.emailservice.event;

import com.ayoam.emailservice.dto.OrderRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderPlacedEvent {
    private List<OrderRequest> orderItemsList;
    private String customerName;

    private String customerEmail;
    private Long orderNumber;
    private Date orderDate;
    private Double orderTotal;
}
