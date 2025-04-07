package com.fpoly.duan.shopdientuv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShopDienTuV2Application {

    public static void main(String[] args) {
        SpringApplication.run(ShopDienTuV2Application.class, args);
    }

}