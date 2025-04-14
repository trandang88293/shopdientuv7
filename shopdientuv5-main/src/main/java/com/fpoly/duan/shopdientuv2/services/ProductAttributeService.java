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
import com.fpoly.duan.shopdientuv2.entitys.Attribute;
import com.fpoly.duan.shopdientuv2.entitys.AttributeValue;
import com.fpoly.duan.shopdientuv2.entitys.Product;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttributeValue;
import com.fpoly.duan.shopdientuv2.jpa.AttributeJPA;
import com.fpoly.duan.shopdientuv2.jpa.AttributeValueJPA;
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
    private AttributeJPA attributeJPA; // Inject repository cho Attribute

    @Autowired
    private AttributeValueJPA attributeValueJPA;

    @Autowired
    private Cloudinary cloudinary;

    // @Transactional
    // public ProductAttribute addProductAttribute(Integer productId,
    // ProductAttribute dto) {
    // Product product = productJPA.findById(productId)
    // .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

    // // Lưu ProductAttribute trước
    // ProductAttribute productAttribute = new ProductAttribute();
    // productAttribute.setProduct(product);
    // productAttribute.setSku(dto.getSku());
    // productAttribute.setPrice(dto.getPrice());
    // productAttribute.setStockQuantity(dto.getStockQuantity());
    // productAttribute.setImageUrl(dto.getImageUrl());

    // productAttribute = productAttributeJPA.save(productAttribute);

    // // Lưu danh sách ProductAttributeValue (nếu có)
    // if (dto.getProductAttributeValues() != null) {
    // for (ProductAttributeValue pav : dto.getProductAttributeValues()) {
    // pav.setProductAttribute(productAttribute);
    // productAttributeValueJPA.save(pav);
    // }
    // }

    // return productAttribute;
    // }

    // @Transactional
    // public ProductAttribute addProductAttribute(Integer productId,
    // ProductAttribute productAttribute) {
    // Product product = productJPA.findById(productId)
    // .orElse(null);

    // if (product == null) {
    // return null; // Hoặc throw exception tùy theo logic của bạn
    // }

    // // Gán sản phẩm vào thuộc tính sản phẩm
    // productAttribute.setProduct(product);
    // ProductAttribute savedProductAttribute =
    // productAttributeJPA.save(productAttribute);

    // // Nếu có các giá trị thuộc tính, lưu chúng
    // if (productAttribute.getProductAttributeValues() != null) {
    // for (ProductAttributeValue productAttributeValue :
    // productAttribute.getProductAttributeValues()) {
    // productAttributeValue.setProductAttribute(savedProductAttribute);
    // productAttributeValueJPA.save(productAttributeValue);
    // }
    // }

    // return savedProductAttribute;
    // }

    @Transactional
    public ProductAttribute addProductAttribute(Integer productId, ProductAttribute productAttribute,
            MultipartFile image) throws IOException {
        Product product = productJPA.findById(productId).orElse(null);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        productAttribute.setProduct(product);

        // Kiểm tra xem biến thể đã tồn tại chưa
        if (isProductAttributeDuplicate(product, productAttribute.getProductAttributeValues())) {
            throw new RuntimeException("The variant is exist");
        }

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

    private boolean isProductAttributeDuplicate(Product product, List<ProductAttributeValue> attributeValuesToCheck) {
        if (product.getProductAttributes() == null || product.getProductAttributes().isEmpty()
                || attributeValuesToCheck == null || attributeValuesToCheck.isEmpty()) {
            return false;
        }

        for (ProductAttribute existingAttribute : product.getProductAttributes()) {
            List<ProductAttributeValue> existingValues = existingAttribute.getProductAttributeValues();
            if (existingValues != null && existingValues.size() == attributeValuesToCheck.size()) {
                boolean match = existingValues.stream()
                        .allMatch(existingValue -> attributeValuesToCheck.stream()
                                .anyMatch(checkValue -> existingValue.getAttribute().getAttributeId()
                                        .equals(checkValue.getAttribute().getAttributeId()) &&
                                        existingValue.getValue().getId().equals(checkValue.getValue().getId())));
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<ProductAttribute> getProductAttributesByProductId(Integer productId) {
        return productAttributeJPA.findByProductId(productId);
    }

    @Transactional
    public ProductAttribute updateProductAttribute(ProductAttribute input) {
        ProductAttribute existing = productAttributeJPA.findById(input.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id = " + input.getId()));

        // Cập nhật các field cơ bản (giữ nguyên)
        existing.setSku(input.getSku());
        existing.setPrice(input.getPrice());
        existing.setStockQuantity(input.getStockQuantity());
        existing.setImageUrl(input.getImageUrl());

        if (input.getProduct() != null && input.getProduct().getProductId() != null) {
            Product product = productJPA.findById(input.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
            existing.setProduct(product);
        }

        List<ProductAttributeValue> newValues = input.getProductAttributeValues();
        List<ProductAttributeValue> existingValues = existing.getProductAttributeValues();

        if (newValues != null) {
            // Xác định các ProductAttributeValue cần xóa
            existingValues.removeIf(existingValue -> newValues.stream()
                    .noneMatch(newValue -> newValue.getAttribute().getAttributeId()
                            .equals(existingValue.getAttribute().getAttributeId()) &&
                            newValue.getValue().getId().equals(existingValue.getValue().getId())));

            // Thêm hoặc cập nhật các ProductAttributeValue mới
            for (ProductAttributeValue newValueFromRequest : newValues) {
                boolean found = false;
                for (ProductAttributeValue existingValue : existingValues) {
                    if (newValueFromRequest.getAttribute().getAttributeId()
                            .equals(existingValue.getAttribute().getAttributeId()) &&
                            newValueFromRequest.getValue().getId().equals(existingValue.getValue().getId())) {
                        // Nếu cần cập nhật thuộc tính của ProductAttributeValue (hiện tại không có)
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Attribute attribute = attributeJPA.findById(newValueFromRequest.getAttribute().getAttributeId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy thuộc tính"));
                    AttributeValue value = attributeValueJPA.findById(newValueFromRequest.getValue().getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy giá trị thuộc tính"));

                    ProductAttributeValue newPav = new ProductAttributeValue();
                    newPav.setProductAttribute(existing);
                    newPav.setAttribute(attribute);
                    newPav.setValue(value);
                    existingValues.add(newPav); // Thêm vào collection được quản lý
                }
            }
            existing.setProductAttributeValues(existingValues);
        } else {
            existing.getProductAttributeValues().clear();
        }

        return productAttributeJPA.save(existing);
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
