package com.fpoly.duan.shopdientuv2.entitys;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attributeId;

    private String attributeName;
    
    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AttributeValue> attributeValues;
}
