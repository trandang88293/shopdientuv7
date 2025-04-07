package com.fpoly.duan.shopdientuv2.dto;

import java.util.List;

public class OrderRequest {
    private Integer addressId;
    private Integer couponId; // có thể null nếu không áp mã giảm giá
    private List<Integer> selectedItems;
    // Loại hình thanh toán: "VNPAY" hoặc "COD"
    private String paymentMethod;

    // Getters & Setters
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Integer> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
