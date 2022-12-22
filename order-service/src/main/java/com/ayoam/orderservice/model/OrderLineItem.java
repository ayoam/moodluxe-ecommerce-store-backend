package com.ayoam.orderservice.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long productId;
    @NotNull
    private String libelle;
    @NotNull
    private Double price;
    @NotNull
    private int quantity;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Photo mainPhoto;

}
