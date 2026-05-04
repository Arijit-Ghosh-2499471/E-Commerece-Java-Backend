package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.repository.ProductRepository;
import com.cts.ecommerce.exception.ResourceNotFoundException;
import com.cts.ecommerce.model.Product;
import com.cts.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ProductService}.
 * Validates that the referenced Category exists before persisting a Product.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating product: {}", product.getProductName());
        validateCategory(product.getCategoryId());
        productRepository.save(product);
        // Re-fetch so categoryName is populated via JOIN
        return productRepository.findById(product.getProductId())
                .orElse(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Integer productId, Product product) {
        log.info("Updating product with id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        validateCategory(product.getCategoryId());
        product.setProductId(productId);
        productRepository.update(product);
        return productRepository.findById(productId).orElse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        log.info("Deleting product with id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Ensures the referenced category exists when one is provided.
     * A null categoryId is allowed because the Products.CategoryId column is nullable.
     */
    private void validateCategory(Integer categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
    }
}
