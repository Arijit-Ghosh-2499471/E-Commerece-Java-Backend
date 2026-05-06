package com.cts.ecommerce.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cts.ecommerce.entity.Review;
import com.cts.ecommerce.exception.ReviewCreationException;
import com.cts.ecommerce.exception.ReviewDeletionException;
import com.cts.ecommerce.exception.ReviewNotFoundException;
import com.cts.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of {@link ReviewRepository}.
 * Provides CRUD operations and query methods for {@link Review} entities
 * using {@link JdbcTemplate}.
 */
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // SQL statements as constants

    /** SQL query to insert a new review */
    private static final String SQL_INSERT =
            "INSERT INTO Review(UserId, ProductId, Rating, ReviewDescription) VALUES (?, ?, ?, ?)";

    /** SQL query to retrieve all reviews */
    private static final String SQL_FIND_ALL =
            "SELECT * FROM Review";

    /** SQL query to retrieve a review by ID */
    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM Review WHERE ReviewId=?";

    /** SQL query to delete a review by ID */
    private static final String SQL_DELETE =
            "DELETE FROM Review WHERE ReviewId=?";

    /** SQL query to retrieve reviews by product ID */
    private static final String SQL_FIND_BY_PRODUCT =
            "SELECT * FROM Review WHERE ProductId=?";

    /** SQL query to retrieve reviews by user ID */
    private static final String SQL_FIND_BY_USER =
            "SELECT * FROM Review WHERE UserId=?";

    /**
     * RowMapper implementation for mapping ResultSet rows to {@link Review} entities.
     */
    private final RowMapper<Review> reviewRowMapper = new RowMapper<Review>() {
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

    /**
     * Saves a new review to the database.
     *
     * @param review the {@link Review} entity to be saved
     * @return number of rows affected
     */
    @Override
    public int save(Review review) {
        try {
            return jdbcTemplate.update(SQL_INSERT,
                    review.getUserId(),
                    review.getProductId(),
                    review.getRating(),
                    review.getReviewDescription());
        } catch (Exception ex) {
            throw new ReviewCreationException("Failed to create review");
        }
    }

    /**
     * Retrieves all reviews in the system.
     *
     * @return list of {@link Review} entities
     */
    @Override
    public List<Review> findAll() {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL, reviewRowMapper);
        } catch (Exception ex) {
            throw new ReviewNotFoundException("Failed to fetch reviews");
        }
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id the review ID
     * @return the {@link Review} entity if found
     */
    @Override
    public Review findById(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, reviewRowMapper, id);
        } catch (Exception ex) {
            throw new ReviewNotFoundException("Review not found with id " + id);
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id the review ID
     * @return number of rows affected
     */
    @Override
    public int delete(int id) {
        try {
            return jdbcTemplate.update(SQL_DELETE, id);
        } catch (Exception ex) {
            throw new ReviewDeletionException("Failed to delete review with id " + id);
        }
    }

    /**
     * Retrieves all reviews associated with a product.
     *
     * @param productId the product ID
     * @return list of {@link Review} entities
     */
    @Override
    public List<Review> findByProductId(int productId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_PRODUCT, reviewRowMapper, productId);
        } catch (Exception ex) {
            throw new ReviewNotFoundException("Reviews not found for productId " + productId);
        }
    }

    /**
     * Retrieves all reviews submitted by a user.
     *
     * @param userId the user ID
     * @return list of {@link Review} entities
     */
    @Override
    public List<Review> findByUserId(int userId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_USER, reviewRowMapper, userId);
        } catch (Exception ex) {
            throw new ReviewNotFoundException("Reviews not found for userId " + userId);
        }
    }
}