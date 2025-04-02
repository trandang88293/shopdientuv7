package com.fpoly.duan.shopdientuv2.entitys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "account")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    private String avatar;

    @NotBlank(message = "Tên không được để trống")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$", message = "Tên không được chứa số và kí tự đặc biệt")
    private String name;

    @NotBlank(message = "Username không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username không được chứa kí tự đặc biệt")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private byte role;
    private boolean isActive;

    @Column(nullable = true)
    private LocalDate birthDate; // Ngày sinh, cho phép null

    @Column(nullable = true)
    private Integer gender; // Giới tính: 0 - Nam, 1 - Nữ, 2 - Khác

    public Account() {
    }
}
