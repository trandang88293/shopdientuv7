package com.fpoly.duan.shopdientuv2.entitys;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    private Integer parentId;
    private Byte rating;
    private String description;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "orderDetailsId")
    private OrderDetails orderDetails;
}
