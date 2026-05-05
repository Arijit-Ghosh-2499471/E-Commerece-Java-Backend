package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.Product;

import java.util.List;

public interface ProductRepository {

    int save(Product product);

    int update(Product product);

    int deleteById(int productId);

    Product findById(int productId);

    List<Product> findAll();

    List<Product> findByCategoryId(int categoryId);

    boolean existsById(int productId);

    List<Product> findByProductNameContaining(String productName);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}