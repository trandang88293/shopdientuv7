package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.entitys.Coupon;
import com.fpoly.duan.shopdientuv2.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "http://localhost:3000")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // Lấy danh sách coupon có hỗ trợ tìm kiếm theo code và phân trang
    @GetMapping
    public Page<Coupon> getAllCoupons(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return couponService.getAllCoupons(keyword, page, size);
    }

    @GetMapping("/{id}")
    public Optional<Coupon> getCouponById(@PathVariable Integer id) {
        return couponService.getCouponById(id);
    }

    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        // Nếu code rỗng hoặc không được cung cấp thì tự động sinh mã coupon ngẫu nhiên
        if (coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            coupon.setCode(generateRandomCode());
        }
        return couponService.createCoupon(coupon);
    }

    @PutMapping("/{id}")
    public Coupon updateCoupon(@PathVariable Integer id, @RequestBody Coupon coupon) {
        // Nếu code rỗng thì sinh lại mã coupon
        if (coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            coupon.setCode(generateRandomCode());
        }
        return couponService.updateCoupon(id, coupon);
    }

    @DeleteMapping("/{id}")
    public String deleteCoupon(@PathVariable Integer id) {
        // Thay vì xóa coupon hoàn toàn, cập nhật số lượng về 0
        couponService.deleteCoupon(id);
        return "Đã cập nhật số lượng coupon về 0 với ID: " + id;
    }

    // Phương thức tạo mã coupon ngẫu nhiên gồm 8 ký tự (có thể điều chỉnh độ dài
    // nếu cần)
    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int codeLength = 8; // độ dài của mã coupon
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
