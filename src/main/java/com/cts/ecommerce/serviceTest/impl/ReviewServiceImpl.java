package com.cts.ecommerce.serviceTest.impl;

import java.util.List;

import com.cts.ecommerce.exception.ReviewCreationException;
import com.cts.ecommerce.exception.ReviewDeletionException;
import com.cts.ecommerce.exception.ReviewNotFoundException;
import com.cts.ecommerce.serviceTest.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cts.ecommerce.entity.Review;
import com.cts.ecommerce.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;

    /**
     * Constructor-based dependency injection.
     *
     * @param reviewRepository review repository object
     */
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Creates a new review.
     *
     * @param review review object to be created
     * @return number of rows affected
     * @throws ReviewCreationException if review creation fails
     */
    @Override
    public int createReview(Review review) {
        try {
            log.info("Creating review for UserId: {} ProductId: {}", review.getUserId(), review.getProductId());

            int result = reviewRepository.save(review);

            if (result == 0) {
                throw new ReviewCreationException("Review creation failed for ProductId: " + review.getProductId());
            }

            return result;

        } catch (ReviewCreationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create review for UserId: {} ProductId: {}",
                    review.getUserId(), review.getProductId(), e);
            throw new ReviewCreationException("Unable to create review", e);
        }
    }

    /**
     * Fetch all reviews.
     *
     * @return list of all reviews
     * @throws ReviewNotFoundException if reviews cannot be fetched
     */
    @Override
    public List<Review> getAllReviews() {
        try {
            log.info("Fetching all reviews");
            return reviewRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to fetch all reviews", e);
            throw new ReviewNotFoundException("Unable to fetch reviews", e);
        }
    }

    /**
     * Fetch review by ID.
     *
     * @param id review ID
     * @return review object
     * @throws ReviewNotFoundException if review is not found
     */
    @Override
    public Review getReviewById(int id) {
        try {
            log.info("Fetching review with ID: {}", id);

            Review review = reviewRepository.findById(id);

            if (review == null) {
                throw new ReviewNotFoundException("Review not found with ID: " + id);
            }

            return review;

        } catch (ReviewNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch review with ID: {}", id, e);
            throw new ReviewNotFoundException("Review not found with ID: " + id, e);
        }
    }

    /**
     * Delete review by ID.
     *
     * @param id review ID
     * @return number of rows affected
     * @throws ReviewDeletionException if review deletion fails
     * @throws ReviewNotFoundException if review is not found
     */
    @Override
    public int deleteReview(int id) {
        try {
            log.info("Deleting review with ID: {}", id);

            int result = reviewRepository.delete(id);

            if (result == 0) {
                throw new ReviewNotFoundException("Review not found with ID: " + id);
            }

            return result;

        } catch (ReviewNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete review with ID: {}", id, e);
            throw new ReviewDeletionException("Unable to delete review with ID: " + id, e);
        }
    }

    /**
     * Fetch reviews by product ID.
     *
     * @param productId product ID
     * @return list of reviews for the product
     * @throws ReviewNotFoundException if reviews cannot be fetched
     */
    @Override
    public List<Review> getReviewByProductId(int productId) {
        try {
            log.info("Fetching reviews for ProductId: {}", productId);
            return reviewRepository.findByProductId(productId);
        } catch (Exception e) {
            log.error("Failed to fetch reviews for ProductId: {}", productId, e);
            throw new ReviewNotFoundException("Unable to fetch reviews for ProductId: " + productId, e);
        }
    }

    /**
     * Fetch reviews by user ID.
     *
     * @param userId user ID
     * @return list of reviews given by the user
     * @throws ReviewNotFoundException if reviews cannot be fetched
     */
    @Override
    public List<Review> getReviewByUserId(int userId) {
        try {
            log.info("Fetching reviews for UserId: {}", userId);
            return reviewRepository.findByUserId(userId);
        } catch (Exception e) {
            log.error("Failed to fetch reviews for UserId: {}", userId, e);
            throw new ReviewNotFoundException("Unable to fetch reviews for UserId: " + userId, e);
        }
    }
}