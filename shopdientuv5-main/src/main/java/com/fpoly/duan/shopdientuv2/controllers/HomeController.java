package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.duan.shopdientuv2.entitys.Product;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.ProductAttributeService;
import com.fpoly.duan.shopdientuv2.services.ProductService;

@RestController
@RequestMapping("/home")
public class HomeController {
    
    @Autowired
    private ProductAttributeService productAttributeService;

    @Autowired
    private ProductService productService;

    @GetMapping("/product/get-all")
    public ResponseEntity<ResponseData> getActiveProductsWithFirstAttribute() {
        ResponseData responseData = new ResponseData();
        try {
            List<Product> products = productAttributeService.getActiveProductsWithAllAttributes();
            responseData.setStatus(true);
            responseData.setMessage("Lấy danh sách sản phẩm thành công!");
            responseData.setData(products);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @GetMapping("/product/get-by-id/{productId}")
    public ResponseEntity<ResponseData> getProductById(@PathVariable Integer productId) {
        ResponseData responseData = new ResponseData();
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                responseData.setStatus(true);
                responseData.setMessage("Lấy sản phẩm thành công");
                responseData.setData(product);
                return ResponseEntity.ok(responseData);
            } else {
                responseData.setStatus(false);
                responseData.setMessage("Sản phẩm không tồn tại");
                responseData.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
