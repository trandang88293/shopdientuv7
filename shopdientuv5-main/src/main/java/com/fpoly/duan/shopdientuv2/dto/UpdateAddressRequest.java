package com.fpoly.duan.shopdientuv2.dto;

import lombok.Data;

@Data
public class UpdateAddressRequest {
    private String username;
    private Integer addressId;
    private String recipientName;
    private String provinceId;
    private String districtId;
    private String wardId;
    private String thirdPartyField;
}
