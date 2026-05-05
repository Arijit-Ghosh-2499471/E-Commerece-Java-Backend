package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Category;
import com.cts.ecommerce.exception.ResourceNotFoundException;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        log.info("Creating category: {}", category.getCategoryName());
        categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(int categoryId, Category category) {
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
    public void deleteCategory(int categoryId) {
        log.info("Deleting category with id: {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(int categoryId) {
        try {
            return categoryRepository.findById(categoryId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}