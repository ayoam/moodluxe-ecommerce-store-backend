package com.ayoam.emailservice.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderStatusChangedEvent {
    private Long orderNumber;
    private String orderStatus;
    private String customerEmail;

}
