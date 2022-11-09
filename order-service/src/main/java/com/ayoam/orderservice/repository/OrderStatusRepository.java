package com.ayoam.orderservice.repository;

import com.ayoam.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Long> {
}
