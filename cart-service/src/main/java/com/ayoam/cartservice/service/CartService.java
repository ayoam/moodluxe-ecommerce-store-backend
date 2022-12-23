package com.ayoam.cartservice.service;

import com.ayoam.cartservice.dto.CartCreatedResponse;
import com.ayoam.cartservice.event.ProductStockChangedEvent;
import com.ayoam.cartservice.model.Cart;
import com.ayoam.cartservice.model.CartItem;
import com.ayoam.cartservice.repository.CartItemRepository;
import com.ayoam.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart getCustomersCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new RuntimeException("cart not found!");
        }

        return cart;
    }

    public Cart addToCart(Long cartId, CartItem cartItem) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new RuntimeException("cart not found");
        }
        CartItem ci = cart.getCartItemList().stream().filter(item->item.getProductId().equals(cartItem.getProductId())).findFirst().orElse(null);
        if(ci==null){
            cart.addToCart(cartItem);
        }else{
            if((ci.getQuantity()+cartItem.getQuantity()<=ci.getProductStock())){
                ci.setQuantity(ci.getQuantity()+cartItem.getQuantity());
            }else{
                ci.setQuantity(ci.getProductStock());
            }
            cartItemRepository.save(ci);
        }
        return cartRepository.save(cart);
    }

    public void removeFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new RuntimeException("cart not found!");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if(cartItem ==null){
            throw new RuntimeException("item not found!");
        }

        cart.removeFromCart(cartItem);
        cartItemRepository.delete(cartItem);
    }

    public CartItem updateCartItemQuantity(Long cartItemId,int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if(cartItem ==null){
            throw new RuntimeException("item not found!");
        }
        if(quantity> cartItem.getProductStock()){
            throw new RuntimeException("only "+cartItem.getProductStock()+" items available!");
        }
        if(!cartItem.isAvailable()){
            throw new RuntimeException("product "+cartItem.getProductId()+" not available at the moment!");
        }
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public void updateCartItemsStock(ProductStockChangedEvent event) {
        List<CartItem> cartItemList = cartItemRepository.findByProductId(event.getProductId()).orElse(null);
        for(CartItem cartItem:cartItemList){
            cartItem.setProductStock(event.getQuantity());
            if(cartItem.getQuantity()>cartItem.getProductStock()){
                cartItem.setQuantity(cartItem.getProductStock());
            }
            cartItemRepository.save(cartItem);
        }
    }

    public void updateCartItemsAvailability(ProductStockChangedEvent event) {
        List<CartItem> cartItemList = cartItemRepository.findByProductId(event.getProductId()).orElse(null);
        for(CartItem cartItem:cartItemList){
            cartItem.setAvailable(false);
            cartItemRepository.save(cartItem);
        }
    }

    public CartCreatedResponse createCart(Cart cart) {
        Cart c = cartRepository.save(cart);
        return new CartCreatedResponse(c.getCartId());
    }

    public Cart addCartItemsList(Long cartId, List<CartItem> cartItemsList) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new RuntimeException("cart not found");
        }

        cartItemsList.forEach(item->{
            CartItem ci = cart.getCartItemList().stream().filter(x->x.getProductId().equals(item.getProductId())).findFirst().orElse(null);
            if(ci==null){
                cart.addToCart(item);
            }else{
                if((ci.getQuantity()+item.getQuantity()<=ci.getProductStock())){
                    ci.setQuantity(ci.getQuantity()+item.getQuantity());
                }else{
                    ci.setQuantity(ci.getProductStock());
                }
                cartItemRepository.save(ci);
            }
        });

        return cartRepository.save(cart);
    }

    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new RuntimeException("cart not found");
        }

        cartItemRepository.deleteAll(cart.getCartItemList());
        cart.setCartItemList(new ArrayList<CartItem>());
        cartRepository.save(cart);
    }
}
