// src/main/java/com/fpoly/duan/shopdientuv2/dto/AttributeDTO.java
package com.fpoly.duan.shopdientuv2.dto;

public class AttributeDTO {
    private Integer attributeId;
    private String attributeName;
    private String attributeValue; // Giá trị của thuộc tính

    // Getters and Setters
    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
