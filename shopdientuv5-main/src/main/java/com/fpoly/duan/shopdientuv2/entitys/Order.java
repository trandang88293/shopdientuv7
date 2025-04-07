package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private LocalDateTime orderDate;
    private Double totalAmount;
    // 0: Đang xử lý, 1: Chờ Xác Nhận, 2: Đang Giao Hàng, 3: Đã giao
    private Integer status;

    // Lưu hình thức thanh toán: "VNPAY" hoặc "COD" hoặc URL thanh toán khi dùng
    // VNPAY
    @Column(length = 1000) // Tăng kích thước cột để chứa URL thanh toán dài hơn
    private String paymentMethod;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetails> orderDetails;
}
