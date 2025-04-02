package com.fpoly.duan.shopdientuv2.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attribute_value")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id", nullable = false)
    @JsonBackReference
    private ProductAttribute productAttribute;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "value_id", nullable = false)
    private AttributeValue value;
}
