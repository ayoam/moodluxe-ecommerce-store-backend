package com.ayoam.customerservice.repository;

import com.ayoam.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    public Optional<Customer> findByEmailIgnoreCase(String email);
    public Optional<Customer> findByKeycloakId(String keycloakId);
}
