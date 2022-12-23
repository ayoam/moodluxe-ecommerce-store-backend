package com.ayoam.orderservice.dto;

import com.ayoam.orderservice.model.OrderAdresse;
import com.ayoam.orderservice.model.OrderLineItem;
import com.ayoam.orderservice.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDto {
    private Long statusId=1L;
    @NotNull
    @JsonProperty("items")
    private List<OrderLineItem> orderLineItemList;

    @NotNull
    @JsonProperty("adresse")
    private OrderAdresse orderAdresse;
    @NotNull
    private Long customerID;

    @NotNull
    private Long cartID;
}
