package com.fpoly.duan.shopdientuv2.jpa;

import com.fpoly.duan.shopdientuv2.entitys.Order;

import java.time.LocalDateTime;
import java.util.List;

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

    List<Order> findByStatusAndOrderDateBefore(Integer status, LocalDateTime time);

    // Hàm tự động sinh SELECT * FROM orders WHERE account.username = ?1
    List<Order> findByAccountUsername(String username);
}
