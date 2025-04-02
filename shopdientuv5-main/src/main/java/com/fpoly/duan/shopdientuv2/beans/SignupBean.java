package com.fpoly.duan.shopdientuv2.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupBean {
    private String avatar;
    private String email;
    private String phone;
    private String name;
    private String username;
    private String password;
    private String cfpassword;
    
}
