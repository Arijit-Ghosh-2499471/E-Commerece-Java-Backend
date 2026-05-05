package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Category;
import com.cts.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Category category) {
        String sql = "INSERT INTO Category(CategoryName) VALUES(?)";
        return jdbcTemplate.update(sql, category.getCategoryName());
    }

    @Override
    public int update(Category category) {
        String sql = "UPDATE Category SET CategoryName=? WHERE CategoryId=?";
        return jdbcTemplate.update(sql, category.getCategoryName(), category.getCategoryId());
    }

    @Override
    public int deleteById(int categoryId) {
        String sql = "DELETE FROM Category WHERE CategoryId=?";
        return jdbcTemplate.update(sql, categoryId);
    }

    @Override
    public Category findById(int categoryId) {
        String sql = "SELECT CategoryId AS categoryId, CategoryName AS categoryName FROM Category WHERE CategoryId=?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Category.class), categoryId);
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT CategoryId AS categoryId, CategoryName AS categoryName FROM Category ORDER BY CategoryId";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public boolean existsById(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Category WHERE CategoryId=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }
}