package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpoly.duan.shopdientuv2.dto.CartAddRequest;
import com.fpoly.duan.shopdientuv2.entitys.CartDetails;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Thêm sản phẩm vào giỏ. Ở đây username được truyền dưới dạng query param.
    @PostMapping("/add")
    public ResponseEntity<ResponseData> addToCart(@RequestParam String username, @RequestBody CartAddRequest request) {
        ResponseData responseData = new ResponseData();
        try {
            CartDetails cartDetails = cartService.addToCart(username, request);
            responseData.setStatus(true);
            responseData.setMessage("Product added to cart successfully!");
            responseData.setData(cartDetails);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Error: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    @PutMapping("/update/{cartDetailsId}")
    public ResponseEntity<ResponseData> updateCartItem(@PathVariable Integer cartDetailsId,
            @RequestParam int quantity) {
        ResponseData responseData = new ResponseData();
        try {
            CartDetails updated = cartService.updateCartItem(cartDetailsId, quantity);
            responseData.setStatus(true);
            responseData.setMessage("Cart item updated successfully!");
            responseData.setData(updated);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Error: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    // Xóa sản phẩm khỏi giỏ
    @DeleteMapping("/delete/{cartDetailsId}")
    public ResponseEntity<ResponseData> deleteCartItem(@PathVariable Integer cartDetailsId) {
        ResponseData responseData = new ResponseData();
        try {
            cartService.deleteCartItem(cartDetailsId);
            responseData.setStatus(true);
            responseData.setMessage("Cart item deleted successfully!");
            responseData.setData(null);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Error: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    // Lấy giỏ hàng của người dùng dựa trên username
    @GetMapping("/get")
    public ResponseEntity<ResponseData> getCart(@RequestParam String username) {
        ResponseData responseData = new ResponseData();
        try {
            List<CartDetails> cartItems = cartService.getCartByUsername(username);
            responseData.setStatus(true);
            responseData.setMessage("Cart retrieved successfully!");
            responseData.setData(cartItems);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Error: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }
}
