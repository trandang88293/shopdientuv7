package com.fpoly.duan.shopdientuv2.entitys;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> product;
}
