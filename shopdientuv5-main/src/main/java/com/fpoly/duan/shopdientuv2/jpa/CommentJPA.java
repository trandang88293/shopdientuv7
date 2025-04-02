package com.fpoly.duan.shopdientuv2.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.duan.shopdientuv2.entitys.Comment;

public interface CommentJPA extends JpaRepository<Comment, Integer> {

}
