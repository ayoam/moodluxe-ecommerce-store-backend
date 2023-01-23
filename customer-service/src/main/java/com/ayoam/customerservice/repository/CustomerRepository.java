package com.ayoam.customerservice.repository;

import com.ayoam.customerservice.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    public Optional<Customer> findByEmailIgnoreCase(String email);
    public Optional<Customer> findByKeycloakId(String keycloakId);
    @Query(value = "SELECT count(idc) FROM customer ")
    public Long customersTotal();

    @Query(value = "select c from customer c where CONCAT(c.firstName, ' ', c.lastName) like :nameFilter or :nameFilter is null")
    public Page<Customer> searchByName(Pageable pages, @Param("nameFilter") String nameFilter);

}
