package com.petbackend.thbao.services;

import com.petbackend.thbao.dtos.CategoryDTO;
import com.petbackend.thbao.models.Category;
import com.petbackend.thbao.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category = Category.builder()
                .name(categoryDTO.getName()).build();

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cannot found category with id = " + id));
        category.setName(categoryDTO.getName());

        return categoryRepository.save(category);

    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
