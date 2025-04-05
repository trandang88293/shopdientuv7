package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartDetailsId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;

    // Tham chiếu đến biến thể sản phẩm (sản phẩm thuộc tính)
    @ManyToOne
    @JoinColumn(name = "productAttributeId")
    private ProductAttribute productAttribute;

    // Nếu có thông tin bổ sung về thuộc tính (chẳng hạn mô tả hoặc JSON)
    @Column(columnDefinition = "TEXT")
    private String attributes;
}
