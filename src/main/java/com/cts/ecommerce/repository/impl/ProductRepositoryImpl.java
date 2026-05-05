package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Product;
import com.cts.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BASE =
            "SELECT p.ProductId AS productId, p.ProductName AS productName, " +
                    "       p.Description AS description, p.Price AS price, " +
                    "       p.CategoryId AS categoryId, p.ImageURL AS imageUrl, " +
                    "       c.CategoryName AS categoryName " +
                    "FROM Products p LEFT JOIN Category c ON p.CategoryId = c.CategoryId ";

    @Override
    public int save(Product product) {
        String sql = "INSERT INTO Products(ProductName, Description, Price, CategoryId, ImageURL) VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getImageUrl());
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE Products SET ProductName=?, Description=?, Price=?, CategoryId=?, ImageURL=? WHERE ProductId=?";
        return jdbcTemplate.update(sql,
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getImageUrl(),
                product.getProductId());
    }

    @Override
    public int deleteById(int productId) {
        String sql = "DELETE FROM Products WHERE ProductId=?";
        return jdbcTemplate.update(sql, productId);
    }

    @Override
    public Product findById(int productId) {
        String sql = SELECT_BASE + "WHERE p.ProductId=?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Product.class), productId);
    }

    @Override
    public List<Product> findAll() {
        String sql = SELECT_BASE + "ORDER BY p.ProductId";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        String sql = SELECT_BASE + "WHERE p.CategoryId=? ORDER BY p.ProductId";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), categoryId);
    }

    @Override
    public boolean existsById(int productId) {
        String sql = "SELECT COUNT(*) FROM Products WHERE ProductId=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count != null && count > 0;
    }

    @Override
    public List<Product> findByProductNameContaining(String productName) {
        String sql = SELECT_BASE + "WHERE p.ProductName LIKE ? ORDER BY p.ProductId";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), "%" + productName + "%");
    }

    @Override
    public List<Product> findByPriceBetween(double minPrice, double maxPrice) {
        String sql = SELECT_BASE + "WHERE p.Price BETWEEN ? AND ? ORDER BY p.Price";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), minPrice, maxPrice);
    }
}