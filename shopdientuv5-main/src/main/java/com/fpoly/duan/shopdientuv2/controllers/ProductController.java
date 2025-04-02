package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.duan.shopdientuv2.entitys.Product;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.ProductService;

@RestController
@RequestMapping("/admin/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData> list() {
        ResponseData responseData = new ResponseData();
        try {
            List<Product> productDTOs = productService.getAllListProducts();
            responseData.setStatus(true);
            responseData.setMessage("");
            responseData.setData(productDTOs);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage(e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ResponseData> getProductById(@PathVariable Integer id) {
        ResponseData responseData = new ResponseData();
        try {
            Product product = productService.getProductById(id);
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

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        ResponseData responseData = new ResponseData();
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            responseData.setStatus(true);
            responseData.setMessage("Cập nhật sản phẩm thành công!");
            responseData.setData(updatedProduct);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage(e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @DeleteMapping("/delete/{productId}")
public ResponseEntity<ResponseData> deleteProduct(@PathVariable Integer productId) {
    ResponseData responseData = new ResponseData();
    try {
        productService.deleteProduct(productId);
        responseData.setStatus(true);
        responseData.setMessage("Sản phẩm ngừng hoạt động!");
        responseData.setData(null);
        return ResponseEntity.ok(responseData);
    } catch (Exception e) {
        responseData.setStatus(false);
        responseData.setMessage("Lỗi: " + e.getMessage());
        responseData.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }
}

}
