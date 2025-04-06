package com.fpoly.duan.shopdientuv2.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Integer addressId;
    private Integer couponId; // có thể null nếu không áp dụng mã giảm giá
}
