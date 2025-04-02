package com.fpoly.duan.shopdientuv2.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpoly.duan.shopdientuv2.entitys.Order;
import com.fpoly.duan.shopdientuv2.entitys.OrderDetails;
import com.fpoly.duan.shopdientuv2.services.OrderDetailService;
import com.fpoly.duan.shopdientuv2.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public OrderController(OrderService orderService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    // Lấy danh sách đơn hàng với tìm kiếm theo tài khoản (username, name) và phân
    // trang
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getAllOrders(keyword, page, size);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<List<OrderDetails>> getOrderDetails(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
    }

    @PostMapping("/{orderId}/details")
    public ResponseEntity<OrderDetails> addOrderDetail(@PathVariable Integer orderId,
            @RequestBody OrderDetails orderDetail) {
        return ResponseEntity.ok(orderDetailService.addOrderDetail(orderId, orderDetail));
    }
}
