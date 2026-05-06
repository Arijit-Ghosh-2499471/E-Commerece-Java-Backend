package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(int productId, Product product);

    void deleteProduct(int productId);

    Product getProductById(int productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(int categoryId);

    List<Product> getProductsByName(String productName);

    List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice);

    void validateProductId(int productId);
}