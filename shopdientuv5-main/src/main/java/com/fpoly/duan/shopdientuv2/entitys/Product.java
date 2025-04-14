package com.fpoly.duan.shopdientuv2.entitys;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    private String name;
    private String description;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Đây là phần hiển thị khi serialize, đối với ProductAttribute sử dụng
                          // @JsonBackReference
    private List<ProductAttribute> productAttributes;
}
