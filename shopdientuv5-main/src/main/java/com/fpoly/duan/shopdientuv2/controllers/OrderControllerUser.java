package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.dto.OrderRequest;
import com.fpoly.duan.shopdientuv2.entitys.Order;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.OrderServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderControllerUser {

    @Autowired
    private OrderServiceUser orderService;

    /**
     * Endpoint để đặt hàng với thông tin truyền qua body (JSON):
     * - addressId: ID địa chỉ giao hàng
     * - couponId: ID mã giảm giá (tuỳ chọn)
     */
    @PostMapping("/place")
    public ResponseEntity<ResponseData> placeOrder(@RequestBody OrderRequest orderRequest) {
        ResponseData responseData = new ResponseData();
        try {
            Order newOrder = orderService.placeOrder(orderRequest);
            responseData.setStatus(true);
            responseData.setMessage("Đặt hàng thành công");
            responseData.setData(newOrder);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi đặt hàng: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.badRequest().body(responseData);
        }
    }
}
