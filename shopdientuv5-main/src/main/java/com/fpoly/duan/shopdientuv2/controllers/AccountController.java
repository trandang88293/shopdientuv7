package com.fpoly.duan.shopdientuv2.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.jpa.AccountJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountJPA accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inject encoder

    @Autowired
    private Cloudinary cloudinary; // Inject Cloudinary

    // ✅ Lấy danh sách tài khoản (tìm kiếm theo tên và username) có phân trang
    @GetMapping
    public ResponseEntity<?> getAdminAccounts(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts;
        // Giả sử vai trò 1 là tài khoản cần lấy (có thể thay đổi theo yêu cầu)
        if (!keyword.isEmpty()) {
            accounts = accountRepository.searchByNameOrUsername((byte) 1, keyword, pageable);
        } else {
            accounts = accountRepository.findByRole((byte) 1, pageable);
        }
        return ResponseEntity.ok(accounts);
    }

    // ✅ Thêm tài khoản với ảnh đại diện (upload lên Cloudinary)
    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("role") byte role,
            @RequestParam("isActive") boolean isActive,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        try {
            // Kiểm tra trùng lặp username và email
            if (accountRepository.existsByUsername(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Username đã tồn tại");
            }
            if (accountRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email đã tồn tại");
            }

            Account account = new Account();
            account.setName(name);
            account.setUsername(username);
            // Mã hóa mật khẩu
            account.setPassword(passwordEncoder.encode(password));
            account.setEmail(email);
            account.setPhone(phone);
            account.setRole(role);
            account.setActive(isActive);

            // Upload ảnh nếu có file được gửi
            if (avatar != null && !avatar.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
                String url = (String) uploadResult.get("url");
                account.setAvatar(url);
            }

            Account savedAccount = accountRepository.save(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tải lên ảnh: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi khi tạo tài khoản: " + e.getMessage());
        }
    }

    // ✅ Lấy thông tin tài khoản để sửa
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable("id") int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }
        return ResponseEntity.ok(optionalAccount.get());
    }

    // ✅ Cập nhật tài khoản (upload ảnh mới lên Cloudinary nếu có thay đổi)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(
            @PathVariable("id") int id,
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("role") byte role,
            @RequestParam("isActive") boolean isActive,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }

        try {
            // Kiểm tra trùng lặp username và email đối với tài khoản khác
            if (accountRepository.existsByUsernameAndAccountIdNot(username, id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Username đã tồn tại");
            }
            if (accountRepository.existsByEmailAndAccountIdNot(email, id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email đã tồn tại");
            }

            Account account = optionalAccount.get();
            account.setName(name);
            account.setUsername(username);
            // Mã hóa mật khẩu mới
            account.setPassword(passwordEncoder.encode(password));
            account.setEmail(email);
            account.setPhone(phone);
            account.setRole(role);
            account.setActive(isActive);

            if (avatar != null && !avatar.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
                String url = (String) uploadResult.get("url");
                account.setAvatar(url);
            }

            accountRepository.save(account);
            return ResponseEntity.ok(account);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tải lên ảnh: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi khi cập nhật tài khoản: " + e.getMessage());
        }
    }

    // ✅ Xóa tài khoản
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }
        accountRepository.deleteById(id);
        return ResponseEntity.ok("Tài khoản đã bị xóa");
    }

    // ✅ Vô hiệu hóa tài khoản
    @PutMapping("/{id}/disable")
    public ResponseEntity<?> disableAccount(@PathVariable("id") int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }
        Account account = optionalAccount.get();
        account.setActive(false);
        accountRepository.save(account);
        return ResponseEntity.ok("Tài khoản đã bị vô hiệu hóa");
    }
}
