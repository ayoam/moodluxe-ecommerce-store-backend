package com.ayoam.orderservice.repository;

import com.ayoam.orderservice.dto.SalesStatistics;
import com.ayoam.orderservice.dto.TotalOrdersAndSalesResponse;
import com.ayoam.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Optional<List<Order>> findByCustomerIDOrderByOrderNumberDesc(Long customerID);
    public Optional<Order> findByInvoiceInvoiceId(Long invoiceID);

    @Query(value = "select new com.ayoam.orderservice.dto.SalesStatistics(cast(DATE_PART('month', OrderDate) as integer),sum(orderTotal)) from orders where date_part('year', OrderDate) = date_part('year', CURRENT_DATE) group by DATE_PART('month', OrderDate) order by DATE_PART('month', OrderDate)")
    public List<SalesStatistics> getSalesStatistics();

    @Query(value = "select new com.ayoam.orderservice.dto.TotalOrdersAndSalesResponse(count(orderNumber),sum(orderTotal)) from orders where date_part('year', OrderDate) = date_part('year', CURRENT_DATE)")
    public TotalOrdersAndSalesResponse getTotalOrdersAndSales();
}
