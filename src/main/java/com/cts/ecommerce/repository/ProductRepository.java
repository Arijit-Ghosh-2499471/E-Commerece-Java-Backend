package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.Product;

import java.util.List;

/**
 * Repository abstraction for managing {@link Product} entities.
 *
 * <p>Defines the contract for persistence operations on the
 * {@code Products} table, including create, read, update, delete,
 * search, and existence checks. Implementations are expected to raise
 * the appropriate user-defined exceptions from
 * {@code com.cts.ecommerce.exception} when an operation fails.</p>
 */
public interface ProductRepository {

    /**
     * Persists a new product.
     *
     * @param product the product to insert; its identifier is generated
     *                by the database
     * @return the number of rows affected (typically {@code 1} on success)
     */
    int save(Product product);

    /**
     * Updates an existing product.
     *
     * @param product the product carrying the new values; its
     *                {@code productId} identifies the row to update
     * @return the number of rows affected ({@code 1} if a matching row
     *         was found, {@code 0} otherwise)
     */
    int update(Product product);

    /**
     * Deletes the product with the given identifier.
     *
     * @param productId the identifier of the product to delete
     * @return the number of rows affected ({@code 1} if a matching row
     *         was found, {@code 0} otherwise)
     */
    int deleteById(int productId);

    /**
     * Retrieves a single product by its identifier, including its
     * associated category name when one is linked.
     *
     * @param productId the identifier of the product to look up
     * @return the matching {@link Product}
     */
    Product findById(int productId);

    /**
     * Retrieves every product in the system, ordered by id.
     *
     * @return a list of all products; never {@code null}, possibly empty
     */
    List<Product> findAll();

    /**
     * Retrieves all products belonging to a given category.
     *
     * @param categoryId the identifier of the category whose products
     *                   should be returned
     * @return a list of matching products; never {@code null},
     *         possibly empty
     */
    List<Product> findByCategoryId(int categoryId);

    /**
     * Checks whether a product with the given identifier exists.
     *
     * @param productId the identifier to check
     * @return {@code true} if a product with that id exists,
     *         {@code false} otherwise
     */
    boolean existsById(int productId);

    /**
     * Searches for products whose name contains the given fragment
     * (case-sensitive {@code LIKE} match).
     *
     * @param productName the substring to search for in product names
     * @return a list of matching products; never {@code null},
     *         possibly empty
     */
    List<Product> findByProductNameContaining(String productName);

    /**
     * Retrieves all products whose price falls within the inclusive
     * range {@code [minPrice, maxPrice]}, sorted by price ascending.
     *
     * @param minPrice the lower bound of the price range (inclusive)
     * @param maxPrice the upper bound of the price range (inclusive)
     * @return a list of matching products; never {@code null},
     *         possibly empty
     */
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}