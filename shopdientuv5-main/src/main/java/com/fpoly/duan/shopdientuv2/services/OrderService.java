package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.entitys.Order;
import com.fpoly.duan.shopdientuv2.jpa.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Lấy danh sách đơn hàng có phân trang và tìm kiếm theo keyword
    public Page<Order> getAllOrders(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null || keyword.trim().isEmpty()) {
            return orderRepository.findAll(pageable);
        } else {
            return orderRepository.searchOrders(keyword, pageable);
        }
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Integer id, Order updatedOrder) {
        Order order = getOrderById(id);
        order.setOrderDate(updatedOrder.getOrderDate());
        order.setTotalAmount(updatedOrder.getTotalAmount());
        order.setStatus(updatedOrder.getStatus());
        return orderRepository.save(order);
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}
