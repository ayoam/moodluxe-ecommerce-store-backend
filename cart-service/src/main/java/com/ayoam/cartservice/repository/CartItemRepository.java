package com.ayoam.cartservice.repository;

import com.ayoam.cartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    public Optional<List<CartItem>> findByProductId(Long productId);
}
