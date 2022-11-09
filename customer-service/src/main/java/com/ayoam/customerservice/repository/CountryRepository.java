package com.ayoam.customerservice.repository;

import com.ayoam.customerservice.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {

}
