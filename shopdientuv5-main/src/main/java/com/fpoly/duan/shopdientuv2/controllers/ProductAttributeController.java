package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.ProductAttributeService;

@RestController
@RequestMapping("/admin/product-attribute")
public class ProductAttributeController {
    
    @Autowired
    private ProductAttributeService productAttributeService;

    @PostMapping("/create/{productId}")
    public ResponseEntity<ProductAttribute> addProductAttribute(
            @PathVariable Integer productId,
            @RequestBody ProductAttribute productAttribute) {

        ProductAttribute savedProductAttribute = productAttributeService.addProductAttribute(productId, productAttribute);

        if (savedProductAttribute == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(savedProductAttribute, HttpStatus.CREATED);
    }

     @GetMapping("/get-by-id/{productId}")
    public ResponseEntity<ResponseData> getProductAttributes(@PathVariable Integer productId) {
        ResponseData responseData = new ResponseData();
        try {
            List<ProductAttribute> productAttributes = productAttributeService.getProductAttributesByProductId(productId);
            if (!productAttributes.isEmpty()) {
                responseData.setStatus(true);
                responseData.setMessage("Lấy biến thể sản phẩm thành công");
                responseData.setData(productAttributes);
                return ResponseEntity.ok(responseData);
            } else {
                responseData.setStatus(false);
                responseData.setMessage("Không tìm thấy biến thể sản phẩm");
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
