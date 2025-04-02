package com.fpoly.duan.shopdientuv2.jpa;

import com.fpoly.duan.shopdientuv2.entitys.Coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Page<Coupon> findByCodeContainingIgnoreCase(String keyword, Pageable pageable);

}
