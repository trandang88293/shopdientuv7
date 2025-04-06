package com.fpoly.duan.shopdientuv2.dto;

import lombok.Data;

@Data
public class ShippingFeeRequest {
    private Integer addressId;
    private Integer weight; // weight tính bằng gram, mặc định 1kg = 1000 gram nếu không có giá trị
}
