package com.ayoam.cartservice.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name="cart")
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    @NotNull
    private Long customerId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> cartItemList = new ArrayList<>();

    public void addToCart(CartItem cartItem){
        this.cartItemList.add(cartItem);
    }

    public void removeFromCart(CartItem cartItem){
        this.cartItemList.remove(cartItem);
    }
}
