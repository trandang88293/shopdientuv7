package com.fpoly.duan.shopdientuv2.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

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
    // 0: Đang xử lý, 1: Chờ Xác Nhận, 2: Đang Giao Hàng, 3: Đã giao, -1: Đã hủy
    private Integer status;

    // Lưu hình thức thanh toán: "VNPAY" hoặc "COD" hoặc URL thanh toán khi dùng
    // VNPAY
    @Column(length = 1000)
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    // Lưu thông tin địa chỉ dạng chuỗi
    @Column(length = 1000)
    private String addressInfo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetails> orderDetails;
}
