package com.ayoam.orderservice.repository;

import com.ayoam.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Optional<List<Order>> findByCustomerID(Long customerID);

}
