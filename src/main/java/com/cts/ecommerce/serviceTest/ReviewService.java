package com.cts.ecommerce.serviceTest;

import java.util.List;
import com.cts.ecommerce.entity.Review;

public interface ReviewService {

    int createReview(Review review);

    List<Review> getAllReviews();

    Review getReviewById(int id);

    int deleteReview(int id);

    List<Review> getReviewByProductId(int productId);

    List<Review> getReviewByUserId(int userId);
}