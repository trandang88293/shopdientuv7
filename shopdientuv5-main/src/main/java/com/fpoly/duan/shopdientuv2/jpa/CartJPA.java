package com.fpoly.duan.shopdientuv2.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.duan.shopdientuv2.entitys.Cart;

public interface CartJPA extends JpaRepository<Cart, Integer> {

}
