package com.cts.ecommerce.exception;

/**
 * Exception thrown when a review is not found.
 */
public class ReviewNotFoundException extends RuntimeException {

    /**
     * Creates a new ReviewNotFoundException with the given message.
     *
     * @param message exception message
     */
    public ReviewNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new ReviewNotFoundException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
