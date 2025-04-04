package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.Product;

@Repository
public interface ProductJPA extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productAttributes WHERE p.isActive = true")
    List<Product> findActiveProductsWithAttributes();
}