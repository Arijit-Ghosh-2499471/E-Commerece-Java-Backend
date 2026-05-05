package com.cts.ecommerce.service.impl;

import java.util.List;

import com.cts.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ecommerce.entity.Review;
import com.cts.ecommerce.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public int createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(int id) {
        return reviewRepository.findById(id);
    }

    @Override
    public int deleteReview(int id) {
        return reviewRepository.delete(id);
    }

    @Override
    public List<Review> getReviewByProductId(int productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public List<Review> getReviewByUserId(int userId) {
        return reviewRepository.findByUserId(userId);
    }
}
