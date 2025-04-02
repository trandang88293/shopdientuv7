package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.AttributeValue;

@Repository
public interface AttributeValueJPA extends JpaRepository<AttributeValue, Integer> {
    
    List<AttributeValue> findByAttribute_AttributeId(Integer attributeId);
}
