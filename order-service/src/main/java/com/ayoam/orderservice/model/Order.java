package com.ayoam.orderservice.model;

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
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue
            (strategy = GenerationType.SEQUENCE,generator = "order_generator")
    @SequenceGenerator
            (name="order_generator", sequenceName = "order_seq",
                    initialValue = 1000,
                    allocationSize=50)
    private Long orderNumber;

    @NotNull
    @ManyToOne
    private OrderStatus status;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItemList;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private OrderAdresse orderAdresse;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Invoice invoice;
    @NotNull
    private Long customerID;

    @CreationTimestamp
    @Column(name = "OrderDate", nullable = false, updatable = false)
    private Date OrderDate;

    private Double orderTotal;

    public Double getOrderTotal(){
        return orderLineItemList.stream().map(item -> item.getQuantity() * item.getPrice()).reduce(0D, Double::sum);
    }

    public void calculateOrderTotal(){
        this.orderTotal = orderLineItemList.stream().map(item -> item.getQuantity() * item.getPrice()).reduce(0D, Double::sum);
    }

}
