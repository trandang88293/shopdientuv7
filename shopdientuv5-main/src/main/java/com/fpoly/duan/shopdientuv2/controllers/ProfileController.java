package com.fpoly.duan.shopdientuv2.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpoly.duan.shopdientuv2.dto.ChangePasswordRequest;
import com.fpoly.duan.shopdientuv2.dto.ProfileUpdateRequest;
import com.fpoly.duan.shopdientuv2.dto.UpdateNameRequest;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.AccountService;

@RestController
@RequestMapping("/account")
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/change-password")
    public ResponseEntity<ResponseData> changePassword(@RequestBody ChangePasswordRequest request) {
        ResponseData responseData = new ResponseData();
        try {
            // Kiểm tra mật khẩu mới nhập 2 lần
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                responseData.setStatus(false);
                responseData.setMessage("Mật khẩu mới không khớp nhau!");
                return ResponseEntity.badRequest().body(responseData);
            }
            // Thực hiện đổi mật khẩu
            boolean changed = accountService.changePassword(
                    request.getUsername(),
                    request.getCurrentPassword(),
                    request.getNewPassword());
            if (changed) {
                responseData.setStatus(true);
                responseData.setMessage("Đổi mật khẩu thành công!");
                return ResponseEntity.ok(responseData);
            } else {
                responseData.setStatus(false);
                responseData.setMessage("Mật khẩu hiện tại không đúng!");
                return ResponseEntity.badRequest().body(responseData);
            }
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    @PostMapping("/update-name")
    public ResponseEntity<ResponseData> updateName(@RequestBody UpdateNameRequest request) {
        ResponseData responseData = new ResponseData();
        try {
            boolean updated = accountService.updateName(request.getUsername(), request.getNewName());
            if (updated) {
                responseData.setStatus(true);
                responseData.setMessage("Cập nhật tên thành công!");
                return ResponseEntity.ok(responseData);
            } else {
                responseData.setStatus(false);
                responseData.setMessage("Không tìm thấy tài khoản!");
                return ResponseEntity.badRequest().body(responseData);
            }
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    @PostMapping("/update-profile")
    public ResponseData updateProfile(@RequestBody ProfileUpdateRequest request) {
        ResponseData response = new ResponseData();
        try {
            boolean updated = accountService.updateProfile(
                    request.getUsername(),
                    request.getName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getGender(),
                    request.getBirthDate());
            if (updated) {
                response.setStatus(true);
                response.setMessage("Cập nhật thông tin thành công!");
            } else {
                response.setStatus(false);
                response.setMessage("Không tìm thấy tài khoản!");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Có lỗi xảy ra: " + e.getMessage());
        }
        return response;
    }

    // Endpoint cập nhật avatar
    @PostMapping("/update-avatar")
    public ResponseEntity<ResponseData> updateAvatar(@RequestParam("username") String username,
            @RequestParam("file") MultipartFile file) {
        ResponseData responseData = new ResponseData();
        try {
            // Upload file lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            boolean updated = accountService.updateAvatar(username, imageUrl);
            if (updated) {
                responseData.setStatus(true);
                responseData.setMessage("Cập nhật avatar thành công!");
                // Gửi trả về URL avatar mới để FE cập nhật
                responseData.setData(imageUrl);
                return ResponseEntity.ok(responseData);
            } else {
                responseData.setStatus(false);
                responseData.setMessage("Không tìm thấy tài khoản!");
                return ResponseEntity.badRequest().body(responseData);
            }
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }
}
