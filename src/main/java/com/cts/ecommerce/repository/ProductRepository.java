package com.cts.ecommerce.repository;


import com.cts.ecommerce.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * DAO contract for the Products table.
 */
public interface ProductRepository {

    int save(Product product);

    int update(Product product);

    int deleteById(Integer productId);

    Optional<Product> findById(Integer productId);

    List<Product> findAll();

    List<Product> findByCategoryId(Integer categoryId);

    boolean existsById(Integer productId);
}
