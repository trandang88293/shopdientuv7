package com.fpoly.duan.shopdientuv2.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fpoly.duan.shopdientuv2.entitys.Address;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Lấy danh sách địa chỉ của tài khoản dựa trên username
    @GetMapping("/get")
    public ResponseEntity<ResponseData> getAddresses(@RequestParam("username") String username) {
        ResponseData responseData = new ResponseData();
        try {
            List<Address> addresses = addressService.getAddressesByUsername(username);
            responseData.setStatus(true);
            responseData.setMessage("Lấy địa chỉ thành công");
            responseData.setData(addresses);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // Thêm địa chỉ mới
    @PostMapping("/add")
    public ResponseEntity<ResponseData> addAddress(@RequestParam("username") String username,
            @RequestBody Address address) {
        ResponseData responseData = new ResponseData();
        try {
            Address newAddress = addressService.addAddress(address, username);
            responseData.setStatus(true);
            responseData.setMessage("Thêm địa chỉ thành công");
            responseData.setData(newAddress);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // Cập nhật địa chỉ
    @PutMapping("/update")
    public ResponseEntity<ResponseData> updateAddress(@RequestParam("username") String username,
            @RequestBody Address address) {
        ResponseData responseData = new ResponseData();
        try {
            Address updatedAddress = addressService.updateAddress(address, username);
            responseData.setStatus(true);
            responseData.setMessage("Cập nhật địa chỉ thành công");
            responseData.setData(updatedAddress);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // Xóa địa chỉ
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData> deleteAddress(@RequestParam("username") String username,
            @RequestParam("addressId") Integer addressId) {
        ResponseData responseData = new ResponseData();
        try {
            addressService.deleteAddress(addressId, username);
            responseData.setStatus(true);
            responseData.setMessage("Xóa địa chỉ thành công");
            responseData.setData(null);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Lỗi: " + e.getMessage());
            responseData.setData(null);
            return ResponseEntity.badRequest().body(responseData);
        }
    }
}
