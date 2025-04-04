package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;

@Repository
public interface ProductAttributeJPA extends JpaRepository<ProductAttribute, Integer> {

    @Query("SELECT pa FROM ProductAttribute pa WHERE pa.product.productId = :productId")
    List<ProductAttribute> findByProductId(Integer productId);

}
