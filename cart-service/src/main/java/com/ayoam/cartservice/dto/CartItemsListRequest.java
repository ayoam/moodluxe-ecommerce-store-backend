package com.ayoam.cartservice.dto;

import com.ayoam.cartservice.model.CartItem;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartItemsListRequest {
    @NotNull
    List<CartItem> cartItems;
}
