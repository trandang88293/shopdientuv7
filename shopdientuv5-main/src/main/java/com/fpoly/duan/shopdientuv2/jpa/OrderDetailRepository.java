package com.fpoly.duan.shopdientuv2.jpa;

import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.entitys.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrderOrderId(Integer orderId);

    Optional<OrderDetails> findTopByOrderAccountAndProductAttribute_IdOrderByOrder_OrderDateDesc(Account account,
            Integer productAttributeId);

}
