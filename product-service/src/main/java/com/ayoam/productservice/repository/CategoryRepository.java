package com.ayoam.productservice.repository;

import com.ayoam.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Optional<Category> findByNameIgnoreCase(String name);
}
