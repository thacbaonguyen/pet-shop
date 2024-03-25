package com.petbackend.thbao.services.impl;

import com.petbackend.thbao.dtos.CategoryDTO;
import com.petbackend.thbao.models.Category;
import com.petbackend.thbao.repositories.CategoryRepository;
import com.petbackend.thbao.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category = Category.builder()
                .name(categoryDTO.getName()).build();

        return categoryRepository.save(category);
    }
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cannot found category with id = " + id));
        category.setName(categoryDTO.getName());

        return categoryRepository.save(category);

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
