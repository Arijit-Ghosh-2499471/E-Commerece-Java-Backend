package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.exception.ResourceNotFoundException;
import com.cts.ecommerce.model.Category;
import com.cts.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link CategoryService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(@NonNull Category category) {
        log.info("Creating category: {}", category.getCategoryName());
        categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(Integer categoryId, Category category) {
        log.info("Updating category with id: {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        category.setCategoryId(categoryId);
        categoryRepository.update(category);
        return category;
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        log.info("Deleting category with id: {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
