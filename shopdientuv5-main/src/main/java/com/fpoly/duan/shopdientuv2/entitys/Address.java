package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    private String recipientName;
    private String phoneNumber;
    private String provinceId;
    private String districtId;
    private String wardId;
    private String thirdPartyField;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId", nullable = true)
    private Account account;
}
