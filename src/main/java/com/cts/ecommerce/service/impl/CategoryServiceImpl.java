package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Category;
import com.cts.ecommerce.exception.CategoryCreationException;
import com.cts.ecommerce.exception.CategoryDeletionException;
import com.cts.ecommerce.exception.CategoryNotFoundException;
import com.cts.ecommerce.exception.CategoryRetrievalException;
import com.cts.ecommerce.exception.CategoryUpdateException;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link CategoryService} that orchestrates
 * persistence operations on {@link Category} entities through the
 * {@link CategoryRepository}.
 * <p>
 * This service confirms that categories exist before update or delete
 * actions, logs every operation through SLF4J, and translates any
 * unexpected failure into the appropriate user-defined exception from
 * {@code com.cts.ecommerce.exception}.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new {@code CategoryServiceImpl} backed by the
     * supplied {@link CategoryRepository}.
     *
     * @param categoryRepository the repository used for category persistence
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new category.
     *
     * @param category the category to create
     * @return the created category
     * @throws CategoryCreationException if the category cannot be persisted
     */
    @Override
    @Transactional
    public Category createCategory(Category category) {
        logger.info("Attempting to create category | Name={}", category.getCategoryName());

        try {
            categoryRepository.save(category);
            logger.info("Category successfully created | Name={}", category.getCategoryName());
            return category;
        } catch (CategoryCreationException ex) {
            logger.error("Create category failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while creating category", ex);
            throw new CategoryCreationException(
                    "Error occurred while creating category", ex);
        }
    }

    /**
     * Updates an existing category after confirming it exists.
     *
     * @param categoryId the identifier of the category to update
     * @param category   the new category data
     * @return the updated category
     * @throws CategoryNotFoundException if no category exists with the given id
     * @throws CategoryUpdateException   if the update operation fails
     */
    @Override
    @Transactional
    public Category updateCategory(int categoryId, Category category) {
        logger.info("Attempting to update category | CategoryId={}", categoryId);

        try {
            if (!categoryRepository.existsById(categoryId)) {
                throw new CategoryNotFoundException(
                        "Category not found with id " + categoryId);
            }
            category.setCategoryId(categoryId);
            categoryRepository.update(category);
            logger.info("Category successfully updated | CategoryId={}", categoryId);
            return category;
        } catch (CategoryNotFoundException ex) {
            logger.warn("Update category skipped: {}", ex.getMessage());
            throw ex;
        } catch (CategoryUpdateException ex) {
            logger.error("Update category failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while updating category | CategoryId={}",
                    categoryId, ex);
            throw new CategoryUpdateException(
                    "Error occurred while updating category", ex);
        }
    }

    /**
     * Deletes the category with the given identifier after confirming
     * it exists.
     *
     * @param categoryId the identifier of the category to delete
     * @throws CategoryNotFoundException if no category exists with the given id
     * @throws CategoryDeletionException if the delete operation fails
     */
    @Override
    @Transactional
    public void deleteCategory(int categoryId) {
        logger.info("Attempting to delete category | CategoryId={}", categoryId);

        try {
            if (!categoryRepository.existsById(categoryId)) {
                throw new CategoryNotFoundException(
                        "Category not found with id " + categoryId);
            }
            categoryRepository.deleteById(categoryId);
            logger.info("Category successfully deleted | CategoryId={}", categoryId);
        } catch (CategoryNotFoundException ex) {
            logger.warn("Delete category skipped: {}", ex.getMessage());
            throw ex;
        } catch (CategoryDeletionException ex) {
            logger.error("Delete category failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting category | CategoryId={}",
                    categoryId, ex);
            throw new CategoryDeletionException(
                    "Error occurred while deleting category", ex);
        }
    }

    /**
     * Retrieves a single category by its identifier.
     *
     * @param categoryId the identifier of the category to fetch
     * @return the matching {@link Category}
     * @throws CategoryNotFoundException if no category exists with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(int categoryId) {
        logger.debug("Fetching category by id | CategoryId={}", categoryId);

        try {
            return categoryRepository.findById(categoryId);
        } catch (CategoryNotFoundException ex) {
            logger.warn("Get category failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching category | CategoryId={}",
                    categoryId, ex);
            throw new CategoryNotFoundException(
                    "Error occurred while fetching category with id " + categoryId, ex);
        }
    }

    /**
     * Retrieves every category in the system.
     *
     * @return a list of all categories; never {@code null}
     * @throws CategoryRetrievalException if the underlying query fails
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        logger.debug("Fetching all categories");

        try {
            return categoryRepository.findAll();
        } catch (CategoryRetrievalException ex) {
            logger.error("Get all categories failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching all categories", ex);
            throw new CategoryRetrievalException(
                    "Error occurred while fetching categories", ex);
        }
    }
}