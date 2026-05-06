package com.cts.ecommerce.exception;

/**
 * Exception thrown when a review cannot be created.
 */
public class ReviewCreationException extends RuntimeException {

    /**
     * Creates a new ReviewCreationException with the given message.
     *
     * @param message exception message
     */
    public ReviewCreationException(String message) {
        super(message);
    }

    /**
     * Creates a new ReviewCreationException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public ReviewCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}