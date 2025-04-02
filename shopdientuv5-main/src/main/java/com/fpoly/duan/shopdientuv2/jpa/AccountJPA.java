package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fpoly.duan.shopdientuv2.entitys.Account;

public interface AccountJPA extends JpaRepository<Account, Integer> {
        List<Account> findByRole(int role);

        Account findByEmail(String email);

        // Lấy danh sách tài khoản theo vai trò với phân trang
        Page<Account> findByRole(byte role, Pageable pageable);

        // Tìm kiếm theo tên và username với vai trò được chỉ định
        @Query("SELECT a FROM Account a WHERE a.role = ?1 AND (a.name LIKE %?2% OR a.username LIKE %?2%)")
        Page<Account> searchByNameOrUsername(byte role, String keyword, Pageable pageable);

        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        boolean existsByUsernameAndAccountIdNot(String username, int accountId);

        boolean existsByEmailAndAccountIdNot(String email, int accountId);

        Account findByUsername(String username);

}
