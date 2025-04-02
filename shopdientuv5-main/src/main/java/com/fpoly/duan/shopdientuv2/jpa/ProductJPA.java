package com.fpoly.duan.shopdientuv2.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.Product;

@Repository
public interface ProductJPA extends JpaRepository<Product, Integer> {
}