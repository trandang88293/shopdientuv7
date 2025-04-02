package com.fpoly.duan.shopdientuv2.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;

@Service
public class UserService {

    private final AccountRepository accountRepository;

    public UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findByUsernameOrEmail(String usernameOrEmail) {
        return accountRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    // Các phương thức khác (đăng ký, cập nhật, ...) có thể bổ sung ở đây
}
