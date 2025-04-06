package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

    // Nếu có cung cấp code, yêu cầu code chỉ chứa chữ in hoa và số (ví dụ)
    @Pattern(regexp = "^[A-Z0-9]*$", message = "Mã coupon chỉ được chứa chữ in hoa và số")
    private String code;

    @NotNull(message = "% Giảm giá không được để trống")
    @Min(value = 1, message = "% Giảm giá phải lớn hơn hoặc bằng 1")
    @Max(value = 100, message = "% Giảm giá không được vượt quá 100")
    private Integer discountPercentage;

    @NotNull(message = "Giá trị giảm tối đa không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm tối đa phải lớn hơn 0")
    private Double maxDiscount;

    @NotNull(message = "Đơn hàng tối thiểu không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Đơn hàng tối thiểu phải lớn hơn 0")
    private Double minOrderValue;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime endDate;

    @NotNull(message = "Số lượng coupon không được để trống")
    @Min(value = 1, message = "Số lượng coupon phải lớn hơn hoặc bằng 1")
    private Integer quantity;
}
