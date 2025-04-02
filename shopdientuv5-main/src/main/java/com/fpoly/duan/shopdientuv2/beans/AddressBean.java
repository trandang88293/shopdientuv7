package com.fpoly.duan.shopdientuv2.beans;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressBean {

        private Integer addressId;
        @NotBlank(message = "recipientName không được bỏ trống")
        private String recipientName;
        @NotBlank(message = "phoneNumber không được bỏ trống")
        private String phoneNumber;
        @NotBlank(message = "provinceId không được bỏ trống")
        private String provinceId;
        @NotBlank(message = "districtId không được bỏ trống")
        private String districtId;
        @NotBlank(message = "wardId không được bỏ trống")
        private String wardId;
        @NotBlank(message = "thirdPartyField không được bỏ trống")
        private String thirdPartyField;
}
