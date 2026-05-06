package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Review;
import com.cts.ecommerce.exception.ReviewCreationException;
import com.cts.ecommerce.exception.ReviewDeletionException;
import com.cts.ecommerce.exception.ReviewNotFoundException;
import com.cts.ecommerce.repository.ReviewRepository;
import com.cts.ecommerce.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ReviewServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        review = new Review();
        review.setReviewId(1);
        review.setUserId(1);
        review.setProductId(101);
        review.setRating(5);
        review.setReviewDescription("Good product");
    }

    /**
     * Tests successful review creation.
     */
    @Test
    void createReview_ShouldReturnOne_WhenReviewIsCreatedSuccessfully() {
        when(reviewRepository.save(review)).thenReturn(1);

        int result = reviewService.createReview(review);

        assertEquals(1, result);
        verify(reviewRepository, times(1)).save(review);
    }

    /**
     * Tests ReviewCreationException when save returns zero rows.
     */
    @Test
    void createReview_ShouldThrowReviewCreationException_WhenSaveReturnsZero() {
        when(reviewRepository.save(review)).thenReturn(0);

        ReviewCreationException exception = assertThrows(
                ReviewCreationException.class,
                () -> reviewService.createReview(review)
        );

        assertTrue(exception.getMessage().contains("Review creation failed"));
        verify(reviewRepository, times(1)).save(review);
    }

    /**
     * Tests ReviewCreationException when repository fails during save.
     */
    @Test
    void createReview_ShouldThrowReviewCreationException_WhenRepositoryFails() {
        when(reviewRepository.save(review)).thenThrow(new RuntimeException("Database error"));

        ReviewCreationException exception = assertThrows(
                ReviewCreationException.class,
                () -> reviewService.createReview(review)
        );

        assertTrue(exception.getMessage().contains("Unable to create review"));
        verify(reviewRepository, times(1)).save(review);
    }

    /**
     * Tests fetching all reviews successfully.
     */
    @Test
    void getAllReviews_ShouldReturnReviewList_WhenReviewsExist() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getAllReviews();

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(5, reviews.getFirst().getRating());
        verify(reviewRepository, times(1)).findAll();
    }

    /**
     * Tests ReviewNotFoundException when fetching all reviews fails.
     */
    @Test
    void getAllReviews_ShouldThrowReviewNotFoundException_WhenRepositoryFails() {
        when(reviewRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        ReviewNotFoundException exception = assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getAllReviews()
        );

        assertTrue(exception.getMessage().contains("Unable to fetch reviews"));
        verify(reviewRepository, times(1)).findAll();
    }

    /**
     * Tests fetching review by ID successfully.
     */
    @Test
    void getReviewById_ShouldReturnReview_WhenReviewExists() {
        when(reviewRepository.findById(1)).thenReturn(review);

        Review result = reviewService.getReviewById(1);

        assertNotNull(result);
        assertEquals(1, result.getReviewId());
        assertEquals("Good product", result.getReviewDescription());
        verify(reviewRepository, times(1)).findById(1);
    }

    /**
     * Tests ReviewNotFoundException when review by ID returns null.
     */
    @Test
    void getReviewById_ShouldThrowReviewNotFoundException_WhenReviewIsNull() {
        when(reviewRepository.findById(1)).thenReturn(null);

        ReviewNotFoundException exception = assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getReviewById(1)
        );

        assertTrue(exception.getMessage().contains("Review not found with ID"));
        verify(reviewRepository, times(1)).findById(1);
    }

    /**
     * Tests ReviewNotFoundException when repository fails while fetching review by ID.
     */
    @Test
    void getReviewById_ShouldThrowReviewNotFoundException_WhenRepositoryFails() {
        when(reviewRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        ReviewNotFoundException exception = assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getReviewById(1)
        );

        assertTrue(exception.getMessage().contains("Review not found with ID"));
        verify(reviewRepository, times(1)).findById(1);
    }

    /**
     * Tests successful review deletion.
     */
    @Test
    void deleteReview_ShouldReturnOne_WhenReviewIsDeletedSuccessfully() {
        when(reviewRepository.delete(1)).thenReturn(1);

        int result = reviewService.deleteReview(1);

        assertEquals(1, result);
        verify(reviewRepository, times(1)).delete(1);
    }

    /**
     * Tests ReviewNotFoundException when delete returns zero rows.
     */
    @Test
    void deleteReview_ShouldThrowReviewNotFoundException_WhenReviewDoesNotExist() {
        when(reviewRepository.delete(1)).thenReturn(0);

        ReviewNotFoundException exception = assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.deleteReview(1)
        );

        assertTrue(exception.getMessage().contains("Review not found with ID"));
        verify(reviewRepository, times(1)).delete(1);
    }

    /**
     * Tests ReviewDeletionException when repository fails during delete.
     */
    @Test
    void deleteReview_ShouldThrowReviewDeletionException_WhenRepositoryFails() {
        when(reviewRepository.delete(1)).thenThrow(new RuntimeException("Database error"));

        ReviewDeletionException exception = assertThrows(
                ReviewDeletionException.class,
                () -> reviewService.deleteReview(1)
        );

        assertTrue(exception.getMessage().contains("Unable to delete review"));
        verify(reviewRepository, times(1)).delete(1);
    }

    /**
     * Tests fetching reviews by product ID successfully.
     */
    @Test
    void getReviewByProductId_ShouldReturnReviews_WhenReviewsExistForProduct() {
        when(reviewRepository.findByProductId(101)).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewByProductId(101);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(101, reviews.getFirst().getProductId());
        verify(reviewRepository, times(1)).findByProductId(101);
    }

    /**
     * Tests ReviewNotFoundException when fetching reviews by product ID fails.
     */
    @Test
    void getReviewByProductId_ShouldThrowReviewNotFoundException_WhenRepositoryFails() {
        when(reviewRepository.findByProductId(101))
                .thenThrow(new RuntimeException("Database error"));

        ReviewNotFoundException exception = assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getReviewByProductId(101)
        );

        assertTrue(exception.getMessage().contains("Unable to fetch reviews for ProductId"));
        verify(reviewRepository, times(1)).findByProductId(101);
    }

    /**
     * Tests fetching reviews by user ID successfully.
     */
    @Test
    void getReviewByUserId_ShouldReturnReviews_WhenReviewsExistForUser() {
        when(reviewRepository.findByUserId(1)).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewByUserId(1);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(1, reviews.getFirst().getUserId());
        verify(reviewRepository, times(1)).findByUserId(1);
    }

    /**
     * Tests ReviewNotFoundException when fetching reviews by user ID fails.
     */
    @Test
    void getReviewByUserId_ShouldThrowReviewNotFoundException_WhenRepositoryFails() {
        when(reviewRepository.findByUserId(1))
                .thenThrow(new RuntimeException("Database error"));

        ReviewNotFoundException exception = assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getReviewByUserId(1)
        );

        assertTrue(exception.getMessage().contains("Unable to fetch reviews for UserId"));
        verify(reviewRepository, times(1)).findByUserId(1);
    }
}