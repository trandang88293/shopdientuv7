package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.dto.ShippingFeeRequest;
import com.fpoly.duan.shopdientuv2.entitys.Address;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.ShippingService;
import com.fpoly.duan.shopdientuv2.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private AddressService addressService;

    @PostMapping("/calculate")
    public ResponseEntity<ResponseData> calculateShipping(@RequestBody ShippingFeeRequest request) {
        ResponseData response = new ResponseData();
        try {
            Integer addressId = request.getAddressId();
            Integer weight = request.getWeight();
            if (addressId == null) {
                throw new RuntimeException("addressId không được để trống");
            }
            // Nếu weight không được truyền lên, backend sẽ sử dụng trọng lượng mặc định
            // (1kg)
            // Lấy thông tin địa chỉ theo addressId
            Address address = addressService.getAddressById(addressId);
            if (address == null) {
                throw new RuntimeException("Địa chỉ không hợp lệ");
            }
            // Tính phí vận chuyển dựa trên trọng lượng truyền lên
            Integer fee = shippingService.calculateShippingFee(address, weight);

            response.setStatus(true);
            response.setMessage("Tính phí thành công");
            response.setData(fee);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Lỗi: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
