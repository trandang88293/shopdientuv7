package com.fpoly.duan.shopdientuv2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpoly.duan.shopdientuv2.entitys.Product;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttributeValue;
import com.fpoly.duan.shopdientuv2.jpa.ProductAttributeJPA;
import com.fpoly.duan.shopdientuv2.jpa.ProductAttributeValueJPA;
import com.fpoly.duan.shopdientuv2.jpa.ProductJPA;

import jakarta.transaction.Transactional;

@Service
public class ProductAttributeService {

    @Autowired
    private ProductJPA productJPA;

    @Autowired
    private ProductAttributeJPA productAttributeJPA;

    @Autowired
    private ProductAttributeValueJPA productAttributeValueJPA;
    
    //  @Transactional
    // public ProductAttribute addProductAttribute(Integer productId, ProductAttribute dto) {
    //     Product product = productJPA.findById(productId)
    //             .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

    //     // Lưu ProductAttribute trước
    //     ProductAttribute productAttribute = new ProductAttribute();
    //     productAttribute.setProduct(product);
    //     productAttribute.setSku(dto.getSku());
    //     productAttribute.setPrice(dto.getPrice());
    //     productAttribute.setStockQuantity(dto.getStockQuantity());
    //     productAttribute.setImageUrl(dto.getImageUrl());

    //     productAttribute = productAttributeJPA.save(productAttribute);

    //     // Lưu danh sách ProductAttributeValue (nếu có)
    //     if (dto.getProductAttributeValues() != null) {
    //         for (ProductAttributeValue pav : dto.getProductAttributeValues()) {
    //             pav.setProductAttribute(productAttribute);
    //             productAttributeValueJPA.save(pav);
    //         }
    //     }

    //     return productAttribute;
    // }

    @Transactional
    public ProductAttribute addProductAttribute(Integer productId, ProductAttribute productAttribute) {
        Product product = productJPA.findById(productId)
                .orElse(null);

        if (product == null) {
            return null; // Hoặc throw exception tùy theo logic của bạn
        }

        productAttribute.setProduct(product);
        ProductAttribute savedProductAttribute = productAttributeJPA.save(productAttribute);

        if (productAttribute.getProductAttributeValues() != null) {
            for (ProductAttributeValue productAttributeValue : productAttribute.getProductAttributeValues()) {
                productAttributeValue.setProductAttribute(savedProductAttribute);
                productAttributeValueJPA.save(productAttributeValue);
            }
        }

        return savedProductAttribute;
    }

    public List<ProductAttribute> getProductAttributesByProductId(Integer productId) {
        return productAttributeJPA.findByProductId(productId);
    }

}

