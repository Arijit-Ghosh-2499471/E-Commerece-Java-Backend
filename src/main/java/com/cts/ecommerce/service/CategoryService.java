package com.cts.ecommerce.service;

import com.cts.ecommerce.model.Category;

import java.util.List;

/**
 * Service contract for Category business logic.
 */
public interface CategoryService {

    Category createCategory(Category category);

    Category updateCategory(Integer categoryId, Category category);

    void deleteCategory(Integer categoryId);

    Category getCategoryById(Integer categoryId);

    List<Category> getAllCategories();
}
