package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.duan.shopdientuv2.entitys.Cart;
import com.fpoly.duan.shopdientuv2.entitys.CartDetails;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails, Integer> {
    Optional<CartDetails> findByCartAndProductAttribute(Cart cart, ProductAttribute productAttribute);

    List<CartDetails> findByCart(Cart cart);
}
