package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category updateCategory(int categoryId, Category category);

    void deleteCategory(int categoryId);

    Category getCategoryById(int categoryId);

    List<Category> getAllCategories();
}