package com.fpoly.duan.shopdientuv2.services;

import java.util.List;
import java.util.Optional;

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

    public Attribute addAttribute(String attributeName) {
        Optional<Attribute> existingAttribute = attributeJPA.findByAttributeName(attributeName);
        return existingAttribute.orElseGet(() -> {
            Attribute newAttr = new Attribute();
            newAttr.setAttributeName(attributeName);
            return attributeJPA.save(newAttr);
        });
    }

    public AttributeValue addAttributeValue(AttributeValue attributeValue) {
        if (attributeValue.getAttribute() == null || attributeValue.getAttribute().getAttributeId() == null) {
            throw new RuntimeException("Attribute ID is required");
        }

        Optional<Attribute> optionalAttribute = attributeJPA.findById(attributeValue.getAttribute().getAttributeId());
        if (optionalAttribute.isEmpty()) {
            throw new RuntimeException("Attribute not found with ID: " + attributeValue.getAttribute().getAttributeId());
        }

        attributeValue.setAttribute(optionalAttribute.get());
        return attributeValueJPA.save(attributeValue);
    }

    public void deleteAttribute(Integer id) {
        attributeJPA.deleteById(id);
    }
}
