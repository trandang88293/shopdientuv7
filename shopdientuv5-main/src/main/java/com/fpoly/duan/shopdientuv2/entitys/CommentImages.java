package com.fpoly.duan.shopdientuv2.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommentImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;
}
