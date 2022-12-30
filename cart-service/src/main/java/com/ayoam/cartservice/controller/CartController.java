package com.ayoam.cartservice.controller;

import com.ayoam.cartservice.dto.CartCreatedResponse;
import com.ayoam.cartservice.dto.CartItemsListRequest;
import com.ayoam.cartservice.model.Cart;
import com.ayoam.cartservice.model.CartItem;
import com.ayoam.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/carts/{cartId}")
    public ResponseEntity<Cart> getCustomersCart(@PathVariable Long cartId){
            return new ResponseEntity<Cart>(cartService.getCustomersCart(cartId),HttpStatus.OK);
    }

    @PostMapping("/carts")
    public ResponseEntity<CartCreatedResponse> createCart(@RequestBody Cart cart){
        //returns cartId
        return new ResponseEntity<CartCreatedResponse>(cartService.createCart(cart),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/carts/{cartId}/addToCart")
    public ResponseEntity<Cart> addToCart(@PathVariable Long cartId, @RequestBody CartItem cartItem){
        return new ResponseEntity<Cart>(cartService.addToCart(cartId,cartItem),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/carts/{cartId}/addCartItemsList")
    public ResponseEntity<Cart> addCartItemsList(@PathVariable Long cartId, @RequestBody CartItemsListRequest cartItemsListRequest){
        return new ResponseEntity<Cart>(cartService.addCartItemsList(cartId,cartItemsListRequest.getCartItems()),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/carts/{cartId}/cartItems/{cartItemId}/removeFromCart")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId){
        cartService.removeFromCart(cartId,cartItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/carts/{cartId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long cartId){
        cartService.clearCart(cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/carts/cartItems/{cartItemId}/updateQuantity")
    public ResponseEntity<CartItem> updateCartItemQuantity(@PathVariable Long cartItemId,@RequestParam int quantity){
        return new ResponseEntity<CartItem>(cartService.updateCartItemQuantity(cartItemId,quantity),HttpStatus.OK);
    }
}
