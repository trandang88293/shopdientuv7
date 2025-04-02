package com.fpoly.duan.shopdientuv2.jpa;


import com.fpoly.duan.shopdientuv2.entitys.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrderOrderId(Integer orderId);
}
