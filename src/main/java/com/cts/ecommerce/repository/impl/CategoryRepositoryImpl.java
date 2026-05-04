package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Category;
import com.cts.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate-based implementation of {@link CategoryRepository}.
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, rowNum) -> Category.builder()
            .categoryId(rs.getInt("CategoryId"))
            .categoryName(rs.getString("CategoryName"))
            .build();

    @Override
    public int save(Category category) {
        String sql = "INSERT INTO Category (CategoryName) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getCategoryName());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            category.setCategoryId(key.intValue());
            return key.intValue();
        }
        return 0;
    }

    @Override
    public int update(Category category) {
        String sql = "UPDATE Category SET CategoryName = ? WHERE CategoryId = ?";
        return jdbcTemplate.update(sql, category.getCategoryName(), category.getCategoryId());
    }

    @Override
    public int deleteById(Integer categoryId) {
        String sql = "DELETE FROM Category WHERE CategoryId = ?";
        return jdbcTemplate.update(sql, categoryId);
    }

    @Override
    public Optional<Category> findById(Integer categoryId) {
        String sql = "SELECT CategoryId, CategoryName FROM Category WHERE CategoryId = ?";
        try {
            Category category = jdbcTemplate.queryForObject(sql, CATEGORY_ROW_MAPPER, categoryId);
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT CategoryId, CategoryName FROM Category ORDER BY CategoryId";
        return jdbcTemplate.query(sql, CATEGORY_ROW_MAPPER);
    }

    @Override
    public boolean existsById(Integer categoryId) {
        String sql = "SELECT COUNT(*) FROM Category WHERE CategoryId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }
}
