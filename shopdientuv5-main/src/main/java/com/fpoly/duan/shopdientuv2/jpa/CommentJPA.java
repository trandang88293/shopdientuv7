package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fpoly.duan.shopdientuv2.entitys.Comment;

public interface CommentJPA extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.orderDetails.productAttribute.id = :productAttributeId")
    List<Comment> findByProductAttributeId(@Param("productAttributeId") Integer productAttributeId);
}
