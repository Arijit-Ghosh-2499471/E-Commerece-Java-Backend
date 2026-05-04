package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.model.Product;
import com.cts.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate-based implementation of {@link ProductRepository}.
 * Uses LEFT JOIN with Category so the returned Product carries the CategoryName too.
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_BASE =
            "SELECT p.ProductId, p.ProductName, p.Description, p.Price, " +
                    "       p.CategoryId, p.ImageURL, c.CategoryName " +
                    "FROM Products p LEFT JOIN Category c ON p.CategoryId = c.CategoryId ";

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) -> {
        Integer categoryId = rs.getObject("CategoryId", Integer.class);
        return Product.builder()
                .productId(rs.getInt("ProductId"))
                .productName(rs.getString("ProductName"))
                .description(rs.getString("Description"))
                .price(rs.getDouble("Price"))
                .categoryId(categoryId)
                .imageUrl(rs.getString("ImageURL"))
                .categoryName(rs.getString("CategoryName"))
                .build();
    };

    @Override
    public int save(Product product) {
        String sql = "INSERT INTO Products (ProductName, Description, Price, CategoryId, ImageURL) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            if (product.getCategoryId() != null) {
                ps.setInt(4, product.getCategoryId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, product.getImageUrl());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            product.setProductId(key.intValue());
            return key.intValue();
        }
        return 0;
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE Products SET ProductName = ?, Description = ?, Price = ?, " +
                "CategoryId = ?, ImageURL = ? WHERE ProductId = ?";
        return jdbcTemplate.update(sql,
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getImageUrl(),
                product.getProductId());
    }

    @Override
    public int deleteById(Integer productId) {
        String sql = "DELETE FROM Products WHERE ProductId = ?";
        return jdbcTemplate.update(sql, productId);
    }

    @Override
    public Optional<Product> findById(Integer productId) {
        String sql = SELECT_BASE + "WHERE p.ProductId = ?";
        try {
            Product product = jdbcTemplate.queryForObject(sql, PRODUCT_ROW_MAPPER, productId);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = SELECT_BASE + "ORDER BY p.ProductId";
        return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
    }

    @Override
    public List<Product> findByCategoryId(Integer categoryId) {
        String sql = SELECT_BASE + "WHERE p.CategoryId = ? ORDER BY p.ProductId";
        return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER, categoryId);
    }

    @Override
    public boolean existsById(Integer productId) {
        String sql = "SELECT COUNT(*) FROM Products WHERE ProductId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count != null && count > 0;
    }
}
