package com.fpoly.duan.shopdientuv2.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.ProductAttributeValue;

@Repository
public interface ProductAttributeValueJPA extends JpaRepository<ProductAttributeValue,Integer>{
    
}
