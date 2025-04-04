package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.duan.shopdientuv2.entitys.Attribute;
import com.fpoly.duan.shopdientuv2.entitys.AttributeValue;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.AttributeService;

@RestController
@RequestMapping("/admin/attribute")
public class AttributeController {
    
    @Autowired
    private AttributeService attributeService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData> list() {
        ResponseData responseData = new ResponseData();
        try {
            List<Attribute> attributes = attributeService.getAllAttributes();
            responseData.setStatus(true);
            responseData.setMessage("Lấy danh sách thuộc tính thành công!");
            responseData.setData(attributes);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi lấy danh sách thuộc tính: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // API lấy danh sách giá trị của thuộc tính theo ID thuộc tính
    @GetMapping("/{attributeId}/values")
    public ResponseEntity<ResponseData> getAttributeValues(@PathVariable Integer attributeId) {
        ResponseData responseData = new ResponseData();
        try {
            List<AttributeValue> values = attributeService.getAttributeValuesByAttributeId(attributeId);
            responseData.setStatus(true);
            responseData.setMessage("Lấy danh sách giá trị thuộc tính thành công!");
            responseData.setData(values);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi lấy giá trị thuộc tính: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // API thêm thuộc tính
    @PostMapping("/create")
    public ResponseEntity<ResponseData> createAttribute(@RequestParam String attributeName) {
        ResponseData responseData = new ResponseData();
        try {
            Attribute attribute = attributeService.addAttribute(attributeName);
            responseData.setStatus(true);
            responseData.setMessage("Thêm thuộc tính thành công!");
            responseData.setData(attribute);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi thêm thuộc tính: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    @PostMapping("/value/create")
    public ResponseEntity<AttributeValue> addAttributeValue(@RequestBody AttributeValue attributeValue) {
        AttributeValue savedAttributeValue = attributeService.addAttributeValue(attributeValue);
        return ResponseEntity.ok(savedAttributeValue);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAttribute(@PathVariable Integer id) {
        try {
            attributeService.deleteAttribute(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
