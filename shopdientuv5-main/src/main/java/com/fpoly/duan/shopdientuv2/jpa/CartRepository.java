package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.duan.shopdientuv2.entitys.Cart;
import com.fpoly.duan.shopdientuv2.entitys.CartDetails;
import com.fpoly.duan.shopdientuv2.entitys.Account;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByAccount(Account account);

}
