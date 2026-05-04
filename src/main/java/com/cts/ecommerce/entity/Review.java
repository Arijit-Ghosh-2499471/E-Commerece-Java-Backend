package com.cts.ecommerce.entity;
public class Review {

    private int ReviewId;
    private int UserId;
    private int ProductId;
    private int Rating;
    private String ReviewDescription;

    public int getReviewId() { return ReviewId; }
    public void setReviewId(int reviewId) { ReviewId = reviewId; }

    public int getUserId() { return UserId; }
    public void setUserId(int userId) { UserId = userId; }

    public int getProductId() { return ProductId; }
    public void setProductId(int productId) { ProductId = productId; }

    public int getRating() { return Rating; }
    public void setRating(int rating) { Rating = rating; }

    public String getReviewDescription() { return ReviewDescription; }
    public void setReviewDescription(String reviewDescription) { ReviewDescription = reviewDescription; }
}