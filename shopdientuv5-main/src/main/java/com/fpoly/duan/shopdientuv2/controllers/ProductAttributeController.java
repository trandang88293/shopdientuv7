package com.fpoly.duan.shopdientuv2.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpoly.duan.shopdientuv2.entitys.Product;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.ProductAttributeService;

@RestController
@RequestMapping("/admin/product-attribute")
public class ProductAttributeController {
    
    @Autowired
    private ProductAttributeService productAttributeService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/create/{productId}")
    public ResponseEntity<?> addProductAttribute(
            @PathVariable Integer productId,
            @RequestParam("productAttribute") String productAttributeJson,
            @RequestPart("imageUrl") MultipartFile image) {
        try {
            ProductAttribute productAttribute = objectMapper.readValue(productAttributeJson, ProductAttribute.class);
            ProductAttribute savedProductAttribute = productAttributeService.addProductAttribute(productId, productAttribute, image);
            return new ResponseEntity<>(savedProductAttribute, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductAttribute> updateProductAttribute(
            @PathVariable Integer id, 
            @RequestBody ProductAttribute updatedProductAttribute) {
        ProductAttribute productAttribute = productAttributeService.updateProductAttribute(id, updatedProductAttribute);
        
        if (productAttribute != null) {
            return new ResponseEntity<>(productAttribute, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Xóa ProductAttribute
    @DeleteMapping("/delete/{id}")
public ResponseEntity<ResponseData> deleteProductAttribute(@PathVariable Integer id) {
    ResponseData responseData = new ResponseData();
    try {
        boolean deleted = productAttributeService.deleteProductAttribute(id);

        if (deleted) {
            responseData.setStatus(true);
            responseData.setMessage("Xóa biến thể sản phẩm thành công");
            responseData.setData(null); // Không cần dữ liệu trả về trong trường hợp xóa thành công
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData);
        } else {
            responseData.setStatus(false);
            responseData.setMessage("Không tìm thấy biến thể sản phẩm để xóa");
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

@GetMapping("/active-with-variant")
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

}
