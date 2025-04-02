package com.fpoly.duan.shopdientuv2.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.utils.JwtUtil;

@Service
public class LoginService {

        private final AccountService accountService;

        private final JwtUtil jwtUtil;

        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        public LoginService(AccountService accountService, JwtUtil jwtUtil,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
                this.accountService = accountService;
                this.jwtUtil = jwtUtil;
                this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        }

        public String login(String email, String password) {

                Account account = accountService.getByEmail(email);
                if (account != null && bCryptPasswordEncoder.matches(password, account.getPassword())) {
                        return jwtUtil.generateToken(email);
                }
                return null;
        }
}
