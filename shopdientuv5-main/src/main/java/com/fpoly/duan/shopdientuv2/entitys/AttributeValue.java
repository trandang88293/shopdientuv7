package com.fpoly.duan.shopdientuv2.entitys;

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

}