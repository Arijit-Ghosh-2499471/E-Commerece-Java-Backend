package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Review {

    private int reviewId;
    private int userId;
    private int productId;
    private int rating;
    private String reviewDescription;

    public Review(int userId, int productId, int rating, String reviewDescription) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.reviewDescription = reviewDescription;
    }
}