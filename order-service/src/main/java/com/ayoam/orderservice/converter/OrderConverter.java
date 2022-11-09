package com.ayoam.orderservice.converter;

import com.ayoam.orderservice.dto.OrderDto;
import com.ayoam.orderservice.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
    public Order orderDtoToOrder(OrderDto dto){
        Order order = new Order();
        order.setOrderLineItemList(dto.getOrderLineItemList());
        return order;
    }

}
