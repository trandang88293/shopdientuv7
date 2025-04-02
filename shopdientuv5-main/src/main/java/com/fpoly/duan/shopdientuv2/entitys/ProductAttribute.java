package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "product_attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_id")
    private Integer id;

    @Column(name = "sku", unique = true, nullable = false)
    private String sku;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "productAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductAttributeValue> productAttributeValues;
}

