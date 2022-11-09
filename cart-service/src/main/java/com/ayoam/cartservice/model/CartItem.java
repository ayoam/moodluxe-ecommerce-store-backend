package com.ayoam.cartservice.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name="cart_item")
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;
    @NotNull
    private Long productId;
    @NotNull
    private String mainPhoto;
    @NotNull
    private String libelle;
    @NotNull
    private Double price;
    @NotNull
    private int quantity;
    @NotNull
    private int productStock;

    private boolean isAvailable=true;
}
