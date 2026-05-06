package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Entity representing the Review table in the ecommerce schema.
 * Maps to:
 *   Review(ReviewId INT PK, UserId INT FK, ProductId INT FK,
 *      Rating INT, ReviewDescription VARCHAR)
 */

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