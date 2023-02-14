package com.ayoam.customerservice.repository;

import com.ayoam.customerservice.model.ConfirmationToken;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByPasswordResetToken(String passwordResetToken);
    PasswordResetToken findByCustomer(Customer customer);
    List<PasswordResetToken> findAllByCustomer(Customer customer);
}
