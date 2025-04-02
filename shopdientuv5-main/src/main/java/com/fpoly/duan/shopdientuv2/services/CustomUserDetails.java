package com.fpoly.duan.shopdientuv2.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.time.LocalDate;
import java.util.Collection;

public class CustomUserDetails extends User {

    private final String name;
    private final String email;
    private final String phone;
    private final String avatar;
    private final LocalDate birthDate; // Ngày sinh
    private final Integer gender; // Giới tính

    // Constructor
    public CustomUserDetails(String username, String password, boolean isActive,
            Collection<? extends GrantedAuthority> authorities,
            String name, String email, String phone, String avatar,
            LocalDate birthDate, Integer gender) {
        super(username, password, isActive, true, true, true, authorities);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    // Getters for additional fields
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Integer getGender() {
        return gender;
    }
}
