package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Product;
import com.cts.ecommerce.exception.ResourceNotFoundException;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.repository.ProductRepository;
import com.cts.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating product: {}", product.getProductName());
        validateCategory(product.getCategoryId());
        productRepository.save(product);
        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(int productId, Product product) {
        log.info("Updating product with id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        validateCategory(product.getCategoryId());
        product.setProductId(productId);
        productRepository.update(product);
        return product;
    }

    @Override
    @Transactional
    public void deleteProduct(int productId) {
        log.info("Deleting product with id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(int productId) {
        try {
            return productRepository.findById(productId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(int categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId);
    }

    private void validateCategory(Integer categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        return productRepository.findByProductNameContaining(productName.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Both minPrice and maxPrice are required");
        }
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Prices cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public void validateProductId(int productId) {
        if(!productRepository.existsById(productId)){
            throw new RuntimeException("Product not found with id: " + productId);
        };
    }
}