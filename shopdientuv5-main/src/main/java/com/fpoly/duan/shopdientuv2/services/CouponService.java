package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.entitys.Coupon;
import com.fpoly.duan.shopdientuv2.jpa.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    // Lấy tất cả coupon (có thể dùng cho admin)
    public Page<Coupon> getAllCoupons(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return couponRepository.findByCodeContainingIgnoreCase(keyword, pageable);
        } else {
            return couponRepository.findAll(pageable);
        }
    }

    public Optional<Coupon> getCouponById(Integer id) {
        return couponRepository.findById(id);
    }

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Integer id, Coupon newCoupon) {
        return couponRepository.findById(id).map(coupon -> {
            coupon.setCode(newCoupon.getCode());
            coupon.setDiscountPercentage(newCoupon.getDiscountPercentage());
            coupon.setMaxDiscount(newCoupon.getMaxDiscount());
            coupon.setMinOrderValue(newCoupon.getMinOrderValue());
            coupon.setStartDate(newCoupon.getStartDate());
            coupon.setEndDate(newCoupon.getEndDate());
            coupon.setQuantity(newCoupon.getQuantity());
            return couponRepository.save(coupon);
        }).orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public void deleteCoupon(Integer id) {
        couponRepository.deleteById(id);
    }
}
