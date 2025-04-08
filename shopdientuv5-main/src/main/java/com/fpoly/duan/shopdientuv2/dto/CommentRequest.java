package com.fpoly.duan.shopdientuv2.dto;

import java.util.List;

public class CommentRequest {
    // Sử dụng productAttributeId thay vì productId
    private Integer productAttributeId;
    private Byte rating;
    private String comment;
    private List<String> imageUrls;

    public Integer getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Integer productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
