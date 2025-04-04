package com.fpoly.duan.shopdientuv2.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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

    @Autowired
    private Cloudinary cloudinary;
    
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

//     @Transactional
// public ProductAttribute addProductAttribute(Integer productId, ProductAttribute productAttribute) {
//     Product product = productJPA.findById(productId)
//             .orElse(null);

//     if (product == null) {
//         return null; // Hoặc throw exception tùy theo logic của bạn
//     }

//     // Gán sản phẩm vào thuộc tính sản phẩm
//     productAttribute.setProduct(product);
//     ProductAttribute savedProductAttribute = productAttributeJPA.save(productAttribute);

//     // Nếu có các giá trị thuộc tính, lưu chúng
//     if (productAttribute.getProductAttributeValues() != null) {
//         for (ProductAttributeValue productAttributeValue : productAttribute.getProductAttributeValues()) {
//             productAttributeValue.setProductAttribute(savedProductAttribute);
//             productAttributeValueJPA.save(productAttributeValue);
//         }
//     }

//     return savedProductAttribute;
// }


@Transactional
public ProductAttribute addProductAttribute(Integer productId, ProductAttribute productAttribute, MultipartFile image) throws IOException {
    Product product = productJPA.findById(productId).orElse(null);

    if (product == null) {
        throw new RuntimeException("Product not found");
    }

    productAttribute.setProduct(product);

    if (image != null && !image.isEmpty()) {
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");
        productAttribute.setImageUrl(imageUrl);
    }

    ProductAttribute savedProductAttribute = productAttributeJPA.save(productAttribute);

    List<ProductAttributeValue> attributeValues = productAttribute.getProductAttributeValues();
    if (attributeValues != null && !attributeValues.isEmpty()) {
        for (ProductAttributeValue value : attributeValues) {
            value.setProductAttribute(savedProductAttribute);
            productAttributeValueJPA.save(value);
        }
    }

    return savedProductAttribute;
}


    public List<ProductAttribute> getProductAttributesByProductId(Integer productId) {
        return productAttributeJPA.findByProductId(productId);
    }

    public ProductAttribute updateProductAttribute(Integer id, ProductAttribute updatedProductAttribute) {
        Optional<ProductAttribute> optionalProductAttribute = productAttributeJPA.findById(id);
        
        if (optionalProductAttribute.isPresent()) {
            ProductAttribute productAttribute = optionalProductAttribute.get();
            productAttribute.setSku(updatedProductAttribute.getSku());
            productAttribute.setPrice(updatedProductAttribute.getPrice());
            productAttribute.setStockQuantity(updatedProductAttribute.getStockQuantity());
            productAttribute.setImageUrl(updatedProductAttribute.getImageUrl());
            productAttribute.setProduct(updatedProductAttribute.getProduct());
            
            // Cập nhật các giá trị thuộc tính khác nếu cần
            
            return productAttributeJPA.save(productAttribute);
        }
        return null; // Nếu không tìm thấy productAttribute với id này
    }

    public boolean deleteProductAttribute(Integer id) {
        if (productAttributeJPA.existsById(id)) {
            productAttributeJPA.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Product> getActiveProductsWithAllAttributes() {
        return productJPA.findActiveProductsWithAttributes();
    }
}

