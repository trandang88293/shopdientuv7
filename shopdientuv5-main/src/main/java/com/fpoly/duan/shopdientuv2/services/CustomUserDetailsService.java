package com.fpoly.duan.shopdientuv2.services;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import com.fpoly.duan.shopdientuv2.entitys.Account;

public class CustomUserDetailsService implements UserDetailsService {

        private final UserService userService;

        public CustomUserDetailsService(UserService userService) {
                this.userService = userService;
        }

        @Override
        public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
                Account account = userService.findByUsernameOrEmail(usernameOrEmail)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Không tìm thấy tài khoản với thông tin: " + usernameOrEmail));

                // Ánh xạ role thành quyền tương ứng
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                if (account.getRole() == 2) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                } else if (account.getRole() == 3) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                // Trả về CustomUserDetails với thông tin ngày sinh và giới tính
                return new CustomUserDetails(
                                account.getUsername(),
                                account.getPassword(),
                                account.isActive(),
                                authorities,
                                account.getName(),
                                account.getEmail(),
                                account.getPhone(),
                                account.getAvatar(),
                                account.getBirthDate(), // Ngày sinh
                                account.getGender() // Giới tính
                );
        }
}
