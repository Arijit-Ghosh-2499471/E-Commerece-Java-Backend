package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Product;

import java.util.List;

/**
 * Service contract for Product business logic.
 */
public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Integer productId, Product product);

    void deleteProduct(Integer productId);

    Product getProductById(Integer productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Integer categoryId);
}
