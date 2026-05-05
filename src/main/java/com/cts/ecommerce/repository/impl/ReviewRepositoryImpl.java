package com.cts.ecommerce.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cts.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cts.ecommerce.entity.Review;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ RowMapper INSIDE repository
    private RowMapper<Review> reviewRowMapper = new RowMapper<Review>() {
        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            Review review = new Review();
            review.setReviewId(rs.getInt("ReviewId"));
            review.setUserId(rs.getInt("UserId"));
            review.setProductId(rs.getInt("ProductId"));
            review.setRating(rs.getInt("Rating"));
            review.setReviewDescription(rs.getString("ReviewDescription"));
            return review;
        }
    };

    @Override
    public int save(Review review) {
        String sql = "INSERT INTO Review(UserId, ProductId, Rating, ReviewDescription) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                review.getUserId(),
                review.getProductId(),
                review.getRating(),
                review.getReviewDescription());
    }

    @Override
    public List<Review> findAll() {
        String sql = "SELECT * FROM Review";
        return jdbcTemplate.query(sql, reviewRowMapper);
    }

    @Override
    public Review findById(int id) {
        String sql = "SELECT * FROM Review WHERE ReviewId=?";
        return jdbcTemplate.queryForObject(sql, reviewRowMapper, id);
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM Review WHERE ReviewId=?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Review> findByProductId(int productId) {
        String sql = "SELECT * FROM Review WHERE ProductId=?";
        return jdbcTemplate.query(sql, reviewRowMapper, productId);
    }

    @Override
    public List<Review> findByUserId(int userId) {
        String sql = "SELECT * FROM Review WHERE UserId=?";
        return jdbcTemplate.query(sql, reviewRowMapper, userId);
    }
}