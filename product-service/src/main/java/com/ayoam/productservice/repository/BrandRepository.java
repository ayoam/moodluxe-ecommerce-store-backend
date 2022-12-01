package com.ayoam.productservice.repository;

import com.ayoam.productservice.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Long> {

}
