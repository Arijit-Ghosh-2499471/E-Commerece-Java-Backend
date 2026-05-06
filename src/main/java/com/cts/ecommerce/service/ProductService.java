package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Product;

import java.util.List;

/**
 * Service abstraction for product-related business operations.
 *
 * <p>Coordinates persistence operations through the repository layer
 * and applies validation rules (such as confirming the parent
 * category exists, sanitising search input, and bounding price
 * ranges) before delegating to the database.</p>
 */
public interface ProductService {

    /**
     * Creates a new product after validating its category reference.
     *
     * @param product the product to create
     * @return the created product (with any database-generated values
     *         carried back where applicable)
     */
    Product createProduct(Product product);

    /**
     * Updates an existing product. Confirms the product exists and
     * that the supplied category (if any) is valid before persisting.
     *
     * @param productId the identifier of the product to update
     * @param product   the new product data
     * @return the updated product
     */
    Product updateProduct(int productId, Product product);

    /**
     * Deletes the product with the given identifier.
     *
     * @param productId the identifier of the product to delete
     */
    void deleteProduct(int productId);

    /**
     * Retrieves a single product by its identifier.
     *
     * @param productId the identifier of the product to fetch
     * @return the matching {@link Product}
     */
    Product getProductById(int productId);

    /**
     * Retrieves every product in the system.
     *
     * @return a list of all products; never {@code null}, possibly empty
     */
    List<Product> getAllProducts();

    /**
     * Retrieves all products belonging to a given category, after
     * confirming that the category itself exists.
     *
     * @param categoryId the category identifier
     * @return a list of matching products; never {@code null},
     *         possibly empty
     */
    List<Product> getProductsByCategory(int categoryId);

    /**
     * Searches for products whose name contains the given fragment.
     *
     * @param productName the substring to search for; must be
     *                    non-{@code null} and non-blank
     * @return a list of matching products; never {@code null},
     *         possibly empty
     */
    List<Product> getProductsByName(String productName);

    /**
     * Retrieves products whose price falls within the inclusive range
     * {@code [minPrice, maxPrice]}.
     *
     * @param minPrice the lower bound (inclusive); must be non-negative
     * @param maxPrice the upper bound (inclusive); must be non-negative
     *                 and not less than {@code minPrice}
     * @return a list of matching products; never {@code null},
     *         possibly empty
     */
    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);

    /**
     * Verifies that a product with the given identifier exists.
     * Intended as a guard for callers (such as cart operations) that
     * need to confirm the product before referencing it.
     *
     * @param productId the identifier to verify
     */
    void validateProductId(int productId);
}