package com.fpoly.duan.shopdientuv2.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.jpa.AccountJPA;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;
import com.fpoly.duan.shopdientuv2.utils.JwtUtil;

@Service
public class AccountService {
        private final AccountJPA accountJPA;
        @Autowired
        private AccountRepository accountRepository;
        private final JwtUtil jwtUtil;
        private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        public AccountService(AccountJPA accountJPA, JwtUtil jwtUtil) {
                this.accountJPA = accountJPA;
                this.jwtUtil = jwtUtil;
        }

        public Account getByEmail(String email) {
                return accountJPA.findByEmail(email);
        }

        public Account getAccountByToken(String authorization) {
                String token = authorization.startsWith("Bearer ")
                                ? authorization.substring(7)
                                : authorization;
                String email = jwtUtil.extractUsername(token);
                return getByEmail(email);
        }

        public boolean changePassword(String username, String currentPassword, String newPassword) {
                // Lấy thông tin tài khoản từ database dựa trên username
                Account account = accountJPA.findByUsername(username);
                if (account != null) {
                        // So sánh mật khẩu hiện tại nhập vào với mật khẩu trong database
                        // Nếu bạn lưu mật khẩu dưới dạng hash (mã hoá), dùng passwordEncoder.matches()
                        if (passwordEncoder.matches(currentPassword, account.getPassword())) {
                                // Mã hoá mật khẩu mới trước khi lưu
                                String encodedNewPassword = passwordEncoder.encode(newPassword);
                                account.setPassword(encodedNewPassword);
                                accountJPA.save(account);
                                return true;
                        }
                }
                return false;
        }

        public boolean updateName(String username, String newName) {
                Account account = accountJPA.findByUsername(username);
                if (account != null) {
                        account.setName(newName);
                        accountJPA.save(account);
                        return true;
                }
                return false;
        }

        public boolean updateAvatar(String username, String avatarUrl) {
                Optional<Account> optional = accountRepository.findByUsername(username);
                if (optional.isPresent()) {
                        Account account = optional.get();
                        account.setAvatar(avatarUrl);
                        accountRepository.save(account);
                        return true;
                }
                return false;
        }

        public boolean updateProfile(String username, String name, String email, String phone, Integer gender,
                        String birthDate) {
                Account acc = accountRepository.findByUsername(username)
                                .orElse(null);
                if (acc == null) {
                        return false;
                }
                acc.setName(name);
                acc.setEmail(email);
                acc.setPhone(phone);
                acc.setGender(gender);
                if (birthDate != null && !birthDate.isEmpty()) {
                        acc.setBirthDate(java.time.LocalDate.parse(birthDate));
                } else {
                        acc.setBirthDate(null);
                }
                accountRepository.save(acc);
                return true;
        }

}
