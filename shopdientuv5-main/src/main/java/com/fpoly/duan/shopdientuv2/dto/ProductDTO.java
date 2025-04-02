package com.fpoly.duan.shopdientuv2.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    private Integer productId;
    private String name;
    private String description;
    private Double price;
    private Boolean isActive;
    private Integer categoryId;  // Chỉ lưu ID của Category để giảm tải dữ liệu
    private List<ProductAttributeDTO> productAttributes;

    public ProductDTO(Integer productId, String name, String description, Double price, Boolean isActive, Integer categoryId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isActive = isActive;
        this.categoryId = categoryId;
    }
    
}
