package com.fpoly.duan.shopdientuv2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.fpoly.duan.shopdientuv2.entitys.Category;
import com.fpoly.duan.shopdientuv2.entitys.Product;
import com.fpoly.duan.shopdientuv2.jpa.CategoryJPA;
import com.fpoly.duan.shopdientuv2.jpa.ProductJPA;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    @Autowired
    private ProductJPA productJPA;

    @Autowired
    private final CategoryJPA categoryJPA;

    @Autowired
    private Cloudinary cloudinary;

    public List<Product> getAllListProducts() {
        return productJPA.findAll();
    }

    public Product addProduct(Product product) {
        Category category = categoryJPA.findById(product.getCategory().getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!"));
        product.setCategory(category);
        product.setIsActive(true);
        return productJPA.save(product);
    }

    public Product getProductById(Integer productId) {
        return productJPA.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));
    }

    public Product updateProduct(Integer id, Product productDetails) {
        return productJPA.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setIsActive(productDetails.getIsActive());
            product.setCategory(categoryJPA.findById(productDetails.getCategory().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!")));
            return productJPA.save(product);
        }).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));
    }

    public void deleteProduct(Integer productId) {
        productJPA.findById(productId).ifPresentOrElse(product -> {
            product.setIsActive(false);
            productJPA.save(product);
        }, () -> {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        });
    }
}
