package com.fpoly.duan.shopdientuv2.config;

import com.fpoly.duan.shopdientuv2.jpa.OrderRepository;
import com.fpoly.duan.shopdientuv2.entitys.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderCleanupTask {

    @Autowired
    private OrderRepository orderRepository;

    // Chạy mỗi phút (60000 ms)
    @Scheduled(fixedRate = 120000)
    public void deleteOldPendingOrders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoMinutesAgo = now.minusMinutes(2);

        List<Order> oldPendingOrders = orderRepository.findByStatusAndOrderDateBefore(0, twoMinutesAgo);

        if (!oldPendingOrders.isEmpty()) {
            orderRepository.deleteAll(oldPendingOrders);
            System.out.println("Đã xóa " + oldPendingOrders.size() + " đơn hàng chờ xử lý quá 2 phút.");
        }
    }
}
