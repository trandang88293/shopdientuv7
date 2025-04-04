package com.fpoly.duan.shopdientuv2.entitys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribute_value")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @OneToMany(mappedBy = "value", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductAttributeValue> productAttributeValues;
}