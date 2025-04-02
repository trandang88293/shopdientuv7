package com.fpoly.duan.shopdientuv2.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dthdblmoj",
                "api_key", "478215135713211",
                "api_secret", "w3k9Lu7wtEJX-G6DQU5L6AfzHIs"));
        return cloudinary;
    }

}
