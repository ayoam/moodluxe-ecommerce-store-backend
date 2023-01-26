package com.ayoam.productservice.repository;

import com.ayoam.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
    @Query(value = "SELECT COALESCE(FLOOR(min(price)/100)*100,0) FROM Product")
    public int minPrice();

    @Query(value = "SELECT COALESCE(CEIL(max(price)/100)*100,0) FROM Product")
    public int maxPrice();

    @Query(value = "SELECT COALESCE(count(idp),0) FROM Product")
    public Long productsTotal();
    @Query("SELECT p FROM Product p ORDER BY p.ordersCount DESC")
    List<Product> findTopSellingItems();
}
