package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Product;
import com.cts.ecommerce.exception.ProductCreationException;
import com.cts.ecommerce.exception.ProductDeletionException;
import com.cts.ecommerce.exception.ProductNotFoundException;
import com.cts.ecommerce.exception.ProductRetrievalException;
import com.cts.ecommerce.exception.ProductUpdateException;
import com.cts.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of {@link ProductRepository}.
 * Provides operations for managing {@link Product} entities
 * using {@link JdbcTemplate}.
 *
 * <p>Each public method wraps its underlying JDBC call in a try/catch
 * block and translates any failure into an operation-specific
 * user-defined exception from {@code com.cts.ecommerce.exception}, so
 * that callers can react to failure modes in a fine-grained way.</p>
 *
 * <p>Read queries left-join the {@code Category} table so that the
 * {@code categoryName} field on the returned {@link Product} is
 * populated whenever the product has an associated category.</p>
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    // SQL statements as constants

    /**
     * Common projection used by every {@code SELECT} in this repository.
     * Joins {@code Products} with {@code Category} so the category name
     * can be populated alongside the product fields.
     */
    private static final String SELECT_PRODUCT_BASE_SQL =
            "SELECT p.ProductId AS productId, p.ProductName AS productName, " +
                    "       p.Description AS description, p.Price AS price, " +
                    "       p.CategoryId AS categoryId, p.ImageURL AS imageUrl, " +
                    "       c.CategoryName AS categoryName " +
                    "FROM Products p LEFT JOIN Category c ON p.CategoryId = c.CategoryId ";

    /** SQL query to insert a new product. */
    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO Products(ProductName, Description, Price, CategoryId, ImageURL) " +
                    "VALUES(?,?,?,?,?)";

    /** SQL query to update an existing product. */
    private static final String UPDATE_PRODUCT_SQL =
            "UPDATE Products SET ProductName=?, Description=?, Price=?, " +
                    "CategoryId=?, ImageURL=? WHERE ProductId=?";

    /** SQL query to delete a product by its identifier. */
    private static final String DELETE_PRODUCT_BY_ID_SQL =
            "DELETE FROM Products WHERE ProductId=?";

    /** SQL query to find a single product by its identifier. */
    private static final String SELECT_PRODUCT_BY_ID_SQL =
            SELECT_PRODUCT_BASE_SQL + "WHERE p.ProductId=?";

    /** SQL query to retrieve all products ordered by id. */
    private static final String SELECT_ALL_PRODUCTS_SQL =
            SELECT_PRODUCT_BASE_SQL + "ORDER BY p.ProductId";

    /** SQL query to retrieve all products belonging to a given category. */
    private static final String SELECT_PRODUCTS_BY_CATEGORY_ID_SQL =
            SELECT_PRODUCT_BASE_SQL + "WHERE p.CategoryId=? ORDER BY p.ProductId";

    /** SQL query to check whether a product with a given id exists. */
    private static final String COUNT_PRODUCT_BY_ID_SQL =
            "SELECT COUNT(*) FROM Products WHERE ProductId=?";

    /** SQL query to search products by a name fragment. */
    private static final String SELECT_PRODUCTS_BY_NAME_LIKE_SQL =
            SELECT_PRODUCT_BASE_SQL + "WHERE p.ProductName LIKE ? ORDER BY p.ProductId";

    /** SQL query to retrieve products whose price falls within a range. */
    private static final String SELECT_PRODUCTS_BY_PRICE_BETWEEN_SQL =
            SELECT_PRODUCT_BASE_SQL + "WHERE p.Price BETWEEN ? AND ? ORDER BY p.Price";

    /**
     * Constructs a new {@code ProductRepositoryImpl} backed by the
     * supplied {@link JdbcTemplate}.
     *
     * @param jdbcTemplate the Spring JDBC template used for all
     *                     database access
     */
    @Autowired
    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Persists a new product.
     *
     * @param product the product to insert
     * @return the number of rows affected (typically {@code 1})
     * @throws ProductCreationException if the insert cannot be performed
     */
    @Override
    public int save(Product product) {
        try {
            return jdbcTemplate.update(
                    INSERT_PRODUCT_SQL,
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategoryId(),
                    product.getImageUrl());
        } catch (Exception ex) {
            throw new ProductCreationException(
                    "Failed to create product with name " + product.getProductName(), ex);
        }
    }

    /**
     * Updates an existing product.
     *
     * @param product the product carrying the new values; its
     *                {@code productId} identifies the target row
     * @return the number of rows affected
     * @throws ProductUpdateException if the update cannot be performed
     */
    @Override
    public int update(Product product) {
        try {
            return jdbcTemplate.update(
                    UPDATE_PRODUCT_SQL,
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategoryId(),
                    product.getImageUrl(),
                    product.getProductId());
        } catch (Exception ex) {
            throw new ProductUpdateException(
                    "Failed to update product with id " + product.getProductId(), ex);
        }
    }

    /**
     * Deletes the product with the given identifier.
     *
     * @param productId the identifier of the product to delete
     * @return the number of rows affected
     * @throws ProductDeletionException if the delete cannot be
     *         performed (e.g. constraint violation)
     */
    @Override
    public int deleteById(int productId) {
        try {
            return jdbcTemplate.update(DELETE_PRODUCT_BY_ID_SQL, productId);
        } catch (Exception ex) {
            throw new ProductDeletionException(
                    "Failed to delete product with id " + productId, ex);
        }
    }

    /**
     * Retrieves a single product by its identifier, including its
     * associated category name when one is linked.
     *
     * @param productId the identifier of the product to look up
     * @return the matching {@link Product}
     * @throws ProductNotFoundException if no row matches the given id
     *         or the query fails
     */
    @Override
    public Product findById(int productId) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_PRODUCT_BY_ID_SQL,
                    new BeanPropertyRowMapper<>(Product.class),
                    productId);
        } catch (Exception ex) {
            throw new ProductNotFoundException(
                    "Product not found with id " + productId, ex);
        }
    }

    /**
     * Retrieves every product ordered by id.
     *
     * @return a list of all products; never {@code null}
     * @throws ProductRetrievalException if the query fails
     */
    @Override
    public List<Product> findAll() {
        try {
            return jdbcTemplate.query(
                    SELECT_ALL_PRODUCTS_SQL,
                    new BeanPropertyRowMapper<>(Product.class));
        } catch (Exception ex) {
            throw new ProductRetrievalException(
                    "Failed to retrieve products", ex);
        }
    }

    /**
     * Retrieves all products belonging to a given category.
     *
     * @param categoryId the identifier of the category whose products
     *                   should be returned
     * @return a list of matching products; never {@code null}
     * @throws ProductRetrievalException if the query fails
     */
    @Override
    public List<Product> findByCategoryId(int categoryId) {
        try {
            return jdbcTemplate.query(
                    SELECT_PRODUCTS_BY_CATEGORY_ID_SQL,
                    new BeanPropertyRowMapper<>(Product.class),
                    categoryId);
        } catch (Exception ex) {
            throw new ProductRetrievalException(
                    "Failed to retrieve products for category id " + categoryId, ex);
        }
    }

    /**
     * Checks whether a product with the given identifier exists.
     *
     * @param productId the identifier to check
     * @return {@code true} if a product with that id exists,
     *         {@code false} otherwise
     * @throws ProductRetrievalException if the existence check fails
     */
    @Override
    public boolean existsById(int productId) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    COUNT_PRODUCT_BY_ID_SQL, Integer.class, productId);
            return count != null && count > 0;
        } catch (Exception ex) {
            throw new ProductRetrievalException(
                    "Failed to check existence of product with id " + productId, ex);
        }
    }

    /**
     * Searches for products whose name contains the given fragment
     * (case-sensitive {@code LIKE} match).
     *
     * @param productName the substring to search for in product names
     * @return a list of matching products; never {@code null}
     * @throws ProductRetrievalException if the query fails
     */
    @Override
    public List<Product> findByProductNameContaining(String productName) {
        try {
            return jdbcTemplate.query(
                    SELECT_PRODUCTS_BY_NAME_LIKE_SQL,
                    new BeanPropertyRowMapper<>(Product.class),
                    "%" + productName + "%");
        } catch (Exception ex) {
            throw new ProductRetrievalException(
                    "Failed to search products by name containing '" + productName + "'", ex);
        }
    }

    /**
     * Retrieves all products whose price falls within the inclusive
     * range {@code [minPrice, maxPrice]}, sorted by price ascending.
     *
     * @param minPrice the lower bound of the price range (inclusive)
     * @param maxPrice the upper bound of the price range (inclusive)
     * @return a list of matching products; never {@code null}
     * @throws ProductRetrievalException if the query fails
     */
    @Override
    public List<Product> findByPriceBetween(double minPrice, double maxPrice) {
        try {
            return jdbcTemplate.query(
                    SELECT_PRODUCTS_BY_PRICE_BETWEEN_SQL,
                    new BeanPropertyRowMapper<>(Product.class),
                    minPrice, maxPrice);
        } catch (Exception ex) {
            throw new ProductRetrievalException(
                    "Failed to retrieve products with price between "
                            + minPrice + " and " + maxPrice, ex);
        }
    }
}