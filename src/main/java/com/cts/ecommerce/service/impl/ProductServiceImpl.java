package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Product;
import com.cts.ecommerce.exception.CategoryNotFoundException;
import com.cts.ecommerce.exception.InvalidInputException;
import com.cts.ecommerce.exception.ProductCreationException;
import com.cts.ecommerce.exception.ProductDeletionException;
import com.cts.ecommerce.exception.ProductNotFoundException;
import com.cts.ecommerce.exception.ProductRetrievalException;
import com.cts.ecommerce.exception.ProductUpdateException;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.repository.ProductRepository;
import com.cts.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ProductService} that orchestrates
 * persistence operations on {@link Product} entities through the
 * {@link ProductRepository}, with category-existence checks performed
 * via the {@link CategoryRepository}.
 * <p>
 * This service validates input, confirms parent categories exist,
 * logs every operation through SLF4J, and translates any unexpected
 * failure into the appropriate user-defined exception from
 * {@code com.cts.ecommerce.exception}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new {@code ProductServiceImpl} backed by the
     * supplied repositories.
     *
     * @param productRepository  repository used for product persistence
     * @param categoryRepository repository used to verify category references
     */
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new product after validating the referenced category.
     *
     * @param product the product to create
     * @return the created product
     * @throws CategoryNotFoundException if the supplied category id does not exist
     * @throws ProductCreationException  if the product cannot be persisted
     */
    @Override
    @Transactional
    public Product createProduct(Product product) {
        logger.info("Attempting to create product | Name={}", product.getProductName());

        try {
            validateCategory(product.getCategoryId());
            productRepository.save(product);
            logger.info("Product successfully created | Name={}", product.getProductName());
            return product;
        } catch (CategoryNotFoundException | ProductCreationException ex) {
            logger.error("Create product failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while creating product", ex);
            throw new ProductCreationException(
                    "Error occurred while creating product", ex);
        }
    }

    /**
     * Updates an existing product after confirming it exists and that
     * the supplied category reference is valid.
     *
     * @param productId the identifier of the product to update
     * @param product   the new product data
     * @return the updated product
     * @throws ProductNotFoundException  if no product exists with the given id
     * @throws CategoryNotFoundException if the supplied category id does not exist
     * @throws ProductUpdateException    if the update operation fails
     */
    @Override
    @Transactional
    public Product updateProduct(int productId, Product product) {
        logger.info("Attempting to update product | ProductId={}", productId);

        try {
            if (!productRepository.existsById(productId)) {
                throw new ProductNotFoundException(
                        "Product not found with id " + productId);
            }
            validateCategory(product.getCategoryId());
            product.setProductId(productId);
            productRepository.update(product);
            logger.info("Product successfully updated | ProductId={}", productId);
            return product;
        } catch (ProductNotFoundException | CategoryNotFoundException ex) {
            logger.warn("Update product skipped: {}", ex.getMessage());
            throw ex;
        } catch (ProductUpdateException ex) {
            logger.error("Update product failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while updating product | ProductId={}",
                    productId, ex);
            throw new ProductUpdateException(
                    "Error occurred while updating product", ex);
        }
    }

    /**
     * Deletes the product with the given identifier after confirming
     * it exists.
     *
     * @param productId the identifier of the product to delete
     * @throws ProductNotFoundException if no product exists with the given id
     * @throws ProductDeletionException if the delete operation fails
     */
    @Override
    @Transactional
    public void deleteProduct(int productId) {
        logger.info("Attempting to delete product | ProductId={}", productId);

        try {
            if (!productRepository.existsById(productId)) {
                throw new ProductNotFoundException(
                        "Product not found with id " + productId);
            }
            productRepository.deleteById(productId);
            logger.info("Product successfully deleted | ProductId={}", productId);
        } catch (ProductNotFoundException ex) {
            logger.warn("Delete product skipped: {}", ex.getMessage());
            throw ex;
        } catch (ProductDeletionException ex) {
            logger.error("Delete product failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting product | ProductId={}",
                    productId, ex);
            throw new ProductDeletionException(
                    "Error occurred while deleting product", ex);
        }
    }

    /**
     * Retrieves a single product by its identifier.
     *
     * @param productId the identifier of the product to fetch
     * @return the matching {@link Product}
     * @throws ProductNotFoundException if no product exists with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public Product getProductById(int productId) {
        logger.debug("Fetching product by id | ProductId={}", productId);

        try {
            return productRepository.findById(productId);
        } catch (ProductNotFoundException ex) {
            logger.warn("Get product failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching product | ProductId={}",
                    productId, ex);
            throw new ProductNotFoundException(
                    "Error occurred while fetching product with id " + productId, ex);
        }
    }

    /**
     * Retrieves every product in the system.
     *
     * @return a list of all products; never {@code null}
     * @throws ProductRetrievalException if the underlying query fails
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");

        try {
            return productRepository.findAll();
        } catch (ProductRetrievalException ex) {
            logger.error("Get all products failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching all products", ex);
            throw new ProductRetrievalException(
                    "Error occurred while fetching products", ex);
        }
    }

    /**
     * Retrieves all products belonging to a given category, after
     * confirming the category itself exists.
     *
     * @param categoryId the category identifier
     * @return a list of matching products; never {@code null}
     * @throws CategoryNotFoundException  if no category exists with the given id
     * @throws ProductRetrievalException  if the underlying query fails
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(int categoryId) {
        logger.debug("Fetching products by category | CategoryId={}", categoryId);

        try {
            if (!categoryRepository.existsById(categoryId)) {
                throw new CategoryNotFoundException(
                        "Category not found with id " + categoryId);
            }
            return productRepository.findByCategoryId(categoryId);
        } catch (CategoryNotFoundException ex) {
            logger.warn("Get products by category skipped: {}", ex.getMessage());
            throw ex;
        } catch (ProductRetrievalException ex) {
            logger.error("Get products by category failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching products | CategoryId={}",
                    categoryId, ex);
            throw new ProductRetrievalException(
                    "Error occurred while fetching products by category id " + categoryId, ex);
        }
    }

    /**
     * Searches for products whose name contains the given fragment
     * (trimmed before passing to the repository).
     *
     * @param productName the substring to search for; must be
     *                    non-{@code null} and non-blank
     * @return a list of matching products; never {@code null}
     * @throws InvalidInputException     if {@code productName} is null or blank
     * @throws ProductRetrievalException if the underlying query fails
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByName(String productName) {
        logger.debug("Searching products by name | Name={}", productName);

        try {
            if (productName == null || productName.trim().isEmpty()) {
                throw new InvalidInputException("Product name cannot be empty");
            }
            return productRepository.findByProductNameContaining(productName.trim());
        } catch (InvalidInputException ex) {
            logger.warn("Search products by name skipped: {}", ex.getMessage());
            throw ex;
        } catch (ProductRetrievalException ex) {
            logger.error("Search products by name failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while searching products by name", ex);
            throw new ProductRetrievalException(
                    "Error occurred while searching products by name", ex);
        }
    }

    /**
     * Retrieves products whose price falls within the inclusive range
     * {@code [minPrice, maxPrice]}.
     *
     * @param minPrice the lower bound (inclusive); must be non-negative
     * @param maxPrice the upper bound (inclusive); must be non-negative
     *                 and not less than {@code minPrice}
     * @return a list of matching products; never {@code null}
     * @throws InvalidInputException     if either bound is null, negative,
     *                                   or {@code minPrice > maxPrice}
     * @throws ProductRetrievalException if the underlying query fails
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        logger.debug("Fetching products by price range | Min={} Max={}", minPrice, maxPrice);

        try {
            if (minPrice == null || maxPrice == null) {
                throw new InvalidInputException("Both minPrice and maxPrice are required");
            }
            if (minPrice < 0 || maxPrice < 0) {
                throw new InvalidInputException("Prices cannot be negative");
            }
            if (minPrice > maxPrice) {
                throw new InvalidInputException(
                        "minPrice cannot be greater than maxPrice");
            }
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        } catch (InvalidInputException ex) {
            logger.warn("Get products by price range skipped: {}", ex.getMessage());
            throw ex;
        } catch (ProductRetrievalException ex) {
            logger.error("Get products by price range failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching products by price range", ex);
            throw new ProductRetrievalException(
                    "Error occurred while fetching products by price range", ex);
        }
    }

    /**
     * Verifies that a product with the given identifier exists.
     *
     * @param productId the identifier to verify
     * @throws ProductNotFoundException if no product exists with the given id
     */
    @Override
    public void validateProductId(int productId) {
        logger.debug("Validating product id | ProductId={}", productId);

        try {
            if (!productRepository.existsById(productId)) {
                throw new ProductNotFoundException(
                        "Product not found with id " + productId);
            }
        } catch (ProductNotFoundException ex) {
            logger.warn("Validate product id failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while validating product id | ProductId={}",
                    productId, ex);
            throw new ProductNotFoundException(
                    "Error occurred while validating product id " + productId, ex);
        }
    }

    /**
     * Confirms that the supplied category id (when non-null) refers
     * to an existing category. A {@code null} id is treated as
     * "no category" and silently accepted, matching the original
     * service contract.
     *
     * @param categoryId the category id to validate, or {@code null}
     * @throws CategoryNotFoundException if a non-null id has no
     *                                   matching category
     */
    private void validateCategory(Integer categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(
                    "Category not found with id " + categoryId);
        }
    }
}