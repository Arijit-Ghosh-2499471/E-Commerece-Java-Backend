package com.cts.ecommerce.repository;

import java.util.List;
import com.cts.ecommerce.entity.Review;

public interface ReviewRepository {

    int save(Review review);

    List<Review> findAll();

    Review findById(int id);

    int delete(int id);
}
