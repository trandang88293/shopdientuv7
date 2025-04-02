package com.fpoly.duan.shopdientuv2.entitys;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    private String imageURL;
    private Boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}