package com.fpoly.duan.shopdientuv2.services;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private Collection<? extends GrantedAuthority> authorities;

    // Các thông tin bổ sung
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private LocalDate birthDate; // Cập nhật kiểu dữ liệu từ String sang LocalDate
    private Integer gender; // Cập nhật kiểu dữ liệu từ String sang Integer

    public CustomUserDetails(String username, String password, boolean active,
            Collection<? extends GrantedAuthority> authorities,
            String name, String email, String phone, String avatar,
            LocalDate birthDate, Integer gender) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Getter cho các thông tin bổ sung
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
