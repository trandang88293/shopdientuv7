package com.fpoly.duan.shopdientuv2.dto;

import lombok.Data;

@Data
public class CreateAddressRequest {
    private String username;
    private String recipientName; // Tuỳ chọn, nếu không cần thì FE có thể gửi chuỗi rỗng
    private String provinceId;
    private String districtId;
    private String wardId;
    private String thirdPartyField; // Thông tin thêm (ví dụ: số nhà, đường,...)
}
