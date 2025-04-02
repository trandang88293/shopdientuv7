package com.fpoly.duan.shopdientuv2.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Tìm theo username
    Optional<Account> findByUsername(String username);

    // Tìm theo email
    Optional<Account> findByEmail(String email);

    // Tìm theo username hoặc email
    default Optional<Account> findByUsernameOrEmail(String usernameOrEmail) {
        Optional<Account> account = findByUsername(usernameOrEmail);
        if (account.isPresent()) {
            return account;
        }
        return findByEmail(usernameOrEmail);
    }

}
