// src/main/java/com/fpoly/duan/shopdientuv2/dto/CommentResponse.java
package com.fpoly.duan.shopdientuv2.dto;

import java.util.List;

public class CommentResponse {
    private Integer commentId;
    private Byte rating;
    private String description;
    private String username;
    private String productName;
    private String productAttributeSKU;
    private List<String> imageUrls;
    // Thêm danh sách thuộc tính của biến thể:
    private List<AttributeDTO> attributes;

    // Getters and Setters
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAttributeSKU() {
        return productAttributeSKU;
    }

    public void setProductAttributeSKU(String productAttributeSKU) {
        this.productAttributeSKU = productAttributeSKU;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDTO> attributes) {
        this.attributes = attributes;
    }
}
