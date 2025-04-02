package com.fpoly.duan.shopdientuv2.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.fpoly.duan.shopdientuv2.dto.ForgotPasswordRequest;
import com.fpoly.duan.shopdientuv2.dto.ResetPasswordRequest;
import com.fpoly.duan.shopdientuv2.dto.VerifyResetCodeRequest;
import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.services.EmailService;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Bước 1: Gửi email mã xác nhận và trả về reset code cho client (chỉ dùng cho
    // demo)
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseData> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ResponseData responseData = new ResponseData();

        Optional<Account> accountOpt = accountRepository.findByUsernameOrEmail(request.getUsernameOrEmail());
        if (!accountOpt.isPresent()) {
            responseData.setStatus(false);
            responseData.setMessage("Không tìm thấy tài khoản với thông tin đã nhập!");
            return ResponseEntity.badRequest().body(responseData);
        }

        Account account = accountOpt.get();
        // Tạo reset code gồm 6 ký tự
        String resetCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6);

        try {
            emailService.sendResetCode(account.getEmail(), resetCode);
            // Ở đây, trả về resetCode cho client để lưu vào Local Storage (dành cho demo)
            responseData.setStatus(true);
            responseData.setMessage("Mã xác nhận đã được gửi tới email: " + account.getEmail());
            responseData.setData(resetCode);
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.setMessage("Có lỗi xảy ra khi gửi email: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // Bước 2: Xác thực reset code từ client (thông thường sẽ được kiểm tra ở phía
    // client trước khi gửi yêu cầu reset mật khẩu)
    @PostMapping("/verify-reset-code")
    public ResponseEntity<ResponseData> verifyResetCode(@RequestBody VerifyResetCodeRequest request) {
        ResponseData responseData = new ResponseData();
        // Với cách lưu trên Local Storage, server không thể kiểm tra trực tiếp.
        // Ta giả định rằng client đã so sánh reset code. Ở đây chỉ xác nhận nhận dữ
        // liệu.
        responseData.setStatus(true);
        responseData.setMessage("Mã xác nhận hợp lệ, vui lòng đặt lại mật khẩu!");
        return ResponseEntity.ok(responseData);
    }

    // Bước 3: Reset mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseData> resetPassword(@RequestBody ResetPasswordRequest request) {
        ResponseData responseData = new ResponseData();

        // Tìm tài khoản bằng username hoặc email
        Optional<Account> accountOpt = accountRepository.findByUsernameOrEmail(request.getUsernameOrEmail());

        if (!accountOpt.isPresent()) {
            responseData.setStatus(false);
            responseData.setMessage("Không tìm thấy tài khoản với thông tin đã nhập!");
            return ResponseEntity.badRequest().body(responseData);
        }

        // Kiểm tra reset code (giả định rằng đã kiểm tra từ phía client)
        Account account = accountOpt.get();

        // Mã hóa mật khẩu mới
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        account.setPassword(encodedPassword); // Cập nhật mật khẩu đã mã hóa
        accountRepository.save(account);

        responseData.setStatus(true);
        responseData.setMessage("Mật khẩu đã được đặt lại thành công!");
        return ResponseEntity.ok(responseData);
    }

}
