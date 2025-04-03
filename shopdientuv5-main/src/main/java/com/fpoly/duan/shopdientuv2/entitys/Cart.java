package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    private Account account;
}
