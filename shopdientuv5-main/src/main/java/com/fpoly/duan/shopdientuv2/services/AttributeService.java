package com.fpoly.duan.shopdientuv2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.entitys.Attribute;
import com.fpoly.duan.shopdientuv2.entitys.AttributeValue;
import com.fpoly.duan.shopdientuv2.jpa.AttributeJPA;
import com.fpoly.duan.shopdientuv2.jpa.AttributeValueJPA;

@Service
public class AttributeService {
    
    @Autowired
    private AttributeJPA attributeJPA;

    @Autowired
    private AttributeValueJPA attributeValueJPA;

    public List<Attribute> getAllAttributes() {
        return attributeJPA.findAll();
    }

    public List<AttributeValue> getAttributeValuesByAttributeId(Integer attributeId) {
        return attributeValueJPA.findByAttribute_AttributeId(attributeId);
    }
}
