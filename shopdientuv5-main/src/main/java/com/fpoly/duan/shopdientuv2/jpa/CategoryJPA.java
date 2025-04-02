package com.fpoly.duan.shopdientuv2.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fpoly.duan.shopdientuv2.dto.CategoryDTO;
import com.fpoly.duan.shopdientuv2.entitys.Category;

public interface CategoryJPA extends JpaRepository<Category, Integer> {
    @Query("SELECT new com.fpoly.duan.shopdientuv2.dto.CategoryDTO(a.categoryId,a.categoryName) "
                        + "FROM Category a ") 
    List<CategoryDTO> Danhsach();

}
