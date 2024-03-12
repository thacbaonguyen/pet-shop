package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.CategoryDTO;
import com.petbackend.thbao.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategory();

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);
}
