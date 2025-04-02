package com.fpoly.duan.shopdientuv2.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.duan.shopdientuv2.entitys.CartDetails;

public interface CartDetailJPA extends JpaRepository<CartDetails, Integer> {

}
