package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.entitys.Order;
import com.fpoly.duan.shopdientuv2.entitys.OrderDetails;
import com.fpoly.duan.shopdientuv2.jpa.OrderDetailRepository;
import com.fpoly.duan.shopdientuv2.jpa.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderDetails> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }

    public OrderDetails addOrderDetail(Integer orderId, OrderDetails orderDetail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderDetail.setOrder(order);
        return orderDetailRepository.save(orderDetail);
    }

    public OrderDetails updateOrderDetail(Integer detailId, OrderDetails updatedDetail) {
        OrderDetails orderDetail = orderDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        orderDetail.setQuantity(updatedDetail.getQuantity());
        orderDetail.setPrice(updatedDetail.getPrice());
        return orderDetailRepository.save(orderDetail);
    }

    public void deleteOrderDetail(Integer detailId) {
        orderDetailRepository.deleteById(detailId);
    }
}
