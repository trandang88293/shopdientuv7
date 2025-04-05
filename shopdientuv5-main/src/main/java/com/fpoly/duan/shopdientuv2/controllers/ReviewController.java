package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.entitys.Comment;
import com.fpoly.duan.shopdientuv2.services.ReviewService;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ResponseData> submitReview(
            @RequestParam("productId") Integer productId,
            @RequestParam("rating") Byte rating,
            @RequestParam("comment") String comment,
            @RequestParam("accountId") Integer accountId,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        ResponseData responseData = new ResponseData();
        try {
            // Gọi hàm trong service để xử lý lưu đánh giá
            Comment newReview = reviewService.submitReview(productId, accountId, rating, comment, images);
            responseData.setStatus(true);
            responseData.setMessage("Đánh giá sản phẩm thành công!");
            responseData.setData(newReview);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
