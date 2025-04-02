package com.fpoly.duan.shopdientuv2.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.duan.shopdientuv2.entitys.Attribute;

@Repository
public interface AttributeJPA extends JpaRepository<Attribute, Integer> {

}
