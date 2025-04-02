package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fpoly.duan.shopdientuv2.beans.CategoryBean;
import com.fpoly.duan.shopdientuv2.dto.CategoryDTO;
import com.fpoly.duan.shopdientuv2.entitys.Category;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/category")
public class CatagoryController {

    @Autowired
    private CategoryService categoryService;

    // 📌 API lấy danh sách danh mục
    @GetMapping("/list")
    public ResponseEntity<ResponseData> list() {
        ResponseData responseData = new ResponseData();
        try {
            List<CategoryDTO> categoryDTOs = categoryService.Danhsach();
            responseData.setStatus(true);
            responseData.setMessage("Lấy danh sách danh mục thành công");
            responseData.setData(categoryDTOs);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi lấy danh sách: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    // 📌 API lưu danh mục (thêm hoặc cập nhật)
    @PostMapping("/save")
    public ResponseEntity<ResponseData> save(@RequestBody @Valid CategoryBean categoryBean, BindingResult result) {
        ResponseData responseData = new ResponseData();
        try {
            // Nếu có lỗi validation
            if (result.hasErrors()) {
                String errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                responseData.setStatus(false);
                responseData.setMessage("Lỗi nhập liệu: " + errorMessages);
                responseData.setData(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            // Tạo entity từ bean
            Category category = new Category();
            category.setCategoryId(categoryBean.getCategoryId());
            category.setCategoryName(categoryBean.getCategoryName());

            boolean isSaved = categoryService.save(category);

            responseData.setStatus(isSaved);
            responseData.setMessage(isSaved ? "Lưu danh mục thành công" : "Lỗi khi lưu danh mục");
            responseData.setData(null);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    // 📌 API xóa danh mục theo ID
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData> delete(@RequestParam Integer categoryId) {
        ResponseData responseData = new ResponseData();
        try {
            boolean isDeleted = categoryService.delete(categoryId);
            responseData.setStatus(isDeleted);
            responseData.setMessage(isDeleted ? "Xóa danh mục thành công" : "Không tìm thấy danh mục cần xóa");
            responseData.setData(null);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi khi xóa danh mục: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }
}
