package com.fpoly.duan.shopdientuv2.dto;

import lombok.Data;

@Data
public class CartAddRequest {
    private Integer productAttributeId;
    private Integer quantity;
    // Dữ liệu thuộc tính có thể ở dạng JSON string hoặc mô tả (tùy theo thiết kế)
    private String attributes;
}
