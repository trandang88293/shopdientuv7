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

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
