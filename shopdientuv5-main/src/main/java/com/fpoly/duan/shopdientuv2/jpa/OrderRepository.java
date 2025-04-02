package com.fpoly.duan.shopdientuv2.jpa;

import com.fpoly.duan.shopdientuv2.entitys.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o " +
            "WHERE LOWER(o.account.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(o.account.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);
}
