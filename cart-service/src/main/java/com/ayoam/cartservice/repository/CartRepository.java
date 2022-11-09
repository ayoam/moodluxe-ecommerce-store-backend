package com.ayoam.cartservice.repository;

import com.ayoam.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    public Optional<Cart> findByCustomerId(Long customerId);
}
