package com.ayoam.productservice.repository;

import com.ayoam.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
    @Query(value = "SELECT FLOOR(min(originalPrice)/100)*100 FROM Product")
    public int minPrice();

    @Query(value = "SELECT CEIL(max(originalPrice)/100)*100 FROM Product")
    public int maxPrice();

    @Query(value = "SELECT count(idp) FROM Product")
    public Long productsTotal();
}
