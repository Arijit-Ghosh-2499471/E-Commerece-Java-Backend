package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Category;
import com.cts.ecommerce.exception.CategoryCreationException;
import com.cts.ecommerce.exception.CategoryDeletionException;
import com.cts.ecommerce.exception.CategoryNotFoundException;
import com.cts.ecommerce.exception.CategoryRetrievalException;
import com.cts.ecommerce.exception.CategoryUpdateException;
import com.cts.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of {@link CategoryRepository}.
 * Provides operations for managing {@link Category} entities
 * using {@link JdbcTemplate}.
 *
 * <p>Each public method wraps its underlying JDBC call in a try/catch
 * block and translates any failure into an operation-specific
 * user-defined exception from {@code com.cts.ecommerce.exception}, so
 * that callers can react to failure modes in a fine-grained way.</p>
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    // SQL statements as constants

    /** SQL query to insert a new category. */
    private static final String INSERT_CATEGORY_SQL =
            "INSERT INTO Category(CategoryName) VALUES(?)";

    /** SQL query to update an existing category. */
    private static final String UPDATE_CATEGORY_SQL =
            "UPDATE Category SET CategoryName=? WHERE CategoryId=?";

    /** SQL query to delete a category by its identifier. */
    private static final String DELETE_CATEGORY_BY_ID_SQL =
            "DELETE FROM Category WHERE CategoryId=?";

    /** SQL query to find a single category by its identifier. */
    private static final String SELECT_CATEGORY_BY_ID_SQL =
            "SELECT CategoryId AS categoryId, CategoryName AS categoryName " +
                    "FROM Category WHERE CategoryId=?";

    /** SQL query to retrieve all categories ordered by id. */
    private static final String SELECT_ALL_CATEGORIES_SQL =
            "SELECT CategoryId AS categoryId, CategoryName AS categoryName " +
                    "FROM Category ORDER BY CategoryId";

    /** SQL query to check whether a category with a given id exists. */
    private static final String COUNT_CATEGORY_BY_ID_SQL =
            "SELECT COUNT(*) FROM Category WHERE CategoryId=?";

    /**
     * Constructs a new {@code CategoryRepositoryImpl} backed by the
     * supplied {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the Spring JDBC template used for all
     *                     database access
     */
    @Autowired
    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Persists a new category.
     *
     * @param category the category to insert
     * @return the number of rows affected (typically {@code 1})
     * @throws CategoryCreationException if the insert cannot be
     *         performed
     */
    @Override
    public int save(Category category) {
        try {
            return jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getCategoryName());
        } catch (Exception ex) {
            throw new CategoryCreationException(
                    "Failed to create category with name " + category.getCategoryName(), ex);
        }
    }

    /**
     * Updates an existing category.
     *
     * @param category the category carrying the new values; its
     *                 {@code categoryId} identifies the target row
     * @return the number of rows affected
     * @throws CategoryUpdateException if the update cannot be performed
     */
    @Override
    public int update(Category category) {
        try {
            return jdbcTemplate.update(
                    UPDATE_CATEGORY_SQL,
                    category.getCategoryName(),
                    category.getCategoryId());
        } catch (Exception ex) {
            throw new CategoryUpdateException(
                    "Failed to update category with id " + category.getCategoryId(), ex);
        }
    }

    /**
     * Deletes the category with the given identifier.
     *
     * @param categoryId the identifier of the category to delete
     * @return the number of rows affected
     * @throws CategoryDeletionException if the delete cannot be
     *         performed (e.g. constraint violation)
     */
    @Override
    public int deleteById(int categoryId) {
        try {
            return jdbcTemplate.update(DELETE_CATEGORY_BY_ID_SQL, categoryId);
        } catch (Exception ex) {
            throw new CategoryDeletionException(
                    "Failed to delete category with id " + categoryId, ex);
        }
    }

    /**
     * Retrieves a single category by its identifier.
     *
     * @param categoryId the identifier of the category to look up
     * @return the matching {@link Category}
     * @throws CategoryNotFoundException if no row matches the given id
     *         or the query fails
     */
    @Override
    public Category findById(int categoryId) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_CATEGORY_BY_ID_SQL,
                    new BeanPropertyRowMapper<>(Category.class),
                    categoryId);
        } catch (Exception ex) {
            throw new CategoryNotFoundException(
                    "Category not found with id " + categoryId, ex);
        }
    }

    /**
     * Retrieves every category ordered by id.
     *
     * @return a list of all categories; never {@code null}
     * @throws CategoryRetrievalException if the query fails
     */
    @Override
    public List<Category> findAll() {
        try {
            return jdbcTemplate.query(
                    SELECT_ALL_CATEGORIES_SQL,
                    new BeanPropertyRowMapper<>(Category.class));
        } catch (Exception ex) {
            throw new CategoryRetrievalException(
                    "Failed to retrieve categories", ex);
        }
    }

    /**
     * Checks whether a category with the given identifier exists.
     *
     * @param categoryId the identifier to check
     * @return {@code true} if a category with that id exists,
     *         {@code false} otherwise
     * @throws CategoryRetrievalException if the existence check fails
     */
    @Override
    public boolean existsById(int categoryId) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    COUNT_CATEGORY_BY_ID_SQL, Integer.class, categoryId);
            return count != null && count > 0;
        } catch (Exception ex) {
            throw new CategoryRetrievalException(
                    "Failed to check existence of category with id " + categoryId, ex);
        }
    }
}