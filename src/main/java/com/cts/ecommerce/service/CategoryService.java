package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Category;

import java.util.List;

/**
 * Service abstraction for category-related business operations.
 *
 * <p>Coordinates persistence operations through the repository layer
 * and enforces business rules around the existence of categories
 * before update or delete actions are performed.</p>
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param category the category to create
     * @return the created category (with any database-generated values
     *         carried back where applicable)
     */
    Category createCategory(Category category);

    /**
     * Updates an existing category, confirming the target exists
     * before persisting.
     *
     * @param categoryId the identifier of the category to update
     * @param category   the new category data
     * @return the updated category
     */
    Category updateCategory(int categoryId, Category category);

    /**
     * Deletes the category with the given identifier.
     *
     * @param categoryId the identifier of the category to delete
     */
    void deleteCategory(int categoryId);

    /**
     * Retrieves a single category by its identifier.
     *
     * @param categoryId the identifier of the category to fetch
     * @return the matching {@link Category}
     */
    Category getCategoryById(int categoryId);

    /**
     * Retrieves every category in the system.
     *
     * @return a list of all categories; never {@code null}, possibly empty
     */
    List<Category> getAllCategories();
}