package com.fpoly.duan.shopdientuv2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.dto.CategoryDTO;
import com.fpoly.duan.shopdientuv2.entitys.Category;
import com.fpoly.duan.shopdientuv2.jpa.CategoryJPA;

@Service
public class CategoryService {
    @Autowired
    private CategoryJPA categoryJPA;

    public List<CategoryDTO> Danhsach() {
        return categoryJPA.Danhsach();
    }
       public boolean save(Category item) {
        categoryJPA.save(item);
                return true;
        }

        public boolean delete(Integer id) {
            Category item= categoryJPA.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("khoong tin thay id " + id));
                                categoryJPA.delete(item);
                return true;
        }
}
