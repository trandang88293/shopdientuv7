package com.fpoly.duan.shopdientuv2.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.fpoly.duan.shopdientuv2.dto.LoginRequest;
import com.fpoly.duan.shopdientuv2.dto.RegisterRequest;
import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.jpa.AccountJPA;
import com.fpoly.duan.shopdientuv2.reps.ResponseData;
import com.fpoly.duan.shopdientuv2.utils.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AccountJPA accountJPA;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            AccountJPA accountJPA, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.accountJPA = accountJPA;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Xác thực dựa trên username/email và password
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Lấy thông tin người dùng đã xác thực
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Sinh token JWT
            String token = jwtUtil.generateToken(userDetails.getUsername());

            // Chuẩn bị dữ liệu trả về cho FE
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("token", token);
            responseBody.put("user", userDetails);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseData(false, "Thông tin đăng nhập không hợp lệ", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (accountJPA.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseData(false, "Username đã tồn tại", null));
        }
        if (accountJPA.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseData(false, "Email đã tồn tại", null));
        }

        // Tạo mới tài khoản với các thông tin từ request và gán giá trị mặc định cho
        // avatar, isActive và role
        Account account = new Account();
        account.setName(registerRequest.getName());
        account.setUsername(registerRequest.getUsername());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setEmail(registerRequest.getEmail());
        account.setPhone(registerRequest.getPhone());
        account.setRole((byte) 1); // Mặc định là người dùng thường
        account.setActive(true); // Mặc định kích hoạt

        // Lưu tài khoản vào CSDL
        accountJPA.save(account);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseData(true, "Đăng ký thành công", account));
    }
}
