package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.dto.OrderRequest;
import com.fpoly.duan.shopdientuv2.entitys.Order;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.OrderServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderControllerUser {

    @Autowired
    private OrderServiceUser orderService;

    @PostMapping("/place")
    public ResponseEntity<ResponseData> placeOrder(@RequestBody OrderRequest orderRequest,
            @RequestParam String username) {
        ResponseData responseData = new ResponseData();
        try {
            Order newOrder = orderService.placeOrder(orderRequest, username);
            responseData.setStatus(true);
            // Nếu thanh toán VNPAY thì trả về URL để FE chuyển hướng
            if ("VNPAY".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
                responseData.setMessage("Đặt hàng thành công, chuyển sang thanh toán VNPAY");
            } else {
                responseData.setMessage("Đặt hàng thành công");
            }
            responseData.setData(newOrder);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi đặt hàng: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.badRequest().body(responseData);
        }
    }
}
