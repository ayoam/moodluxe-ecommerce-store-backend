package com.ayoam.customerservice.repository;

import com.ayoam.customerservice.model.ConfirmationToken;
import com.ayoam.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    ConfirmationToken findByCustomer(Customer customer);
}
