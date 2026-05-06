package com.cts.ecommerce.exception;

/**
 * Exception thrown when a review cannot be deleted.
 */
public class ReviewDeletionException extends RuntimeException {

    /**
     * Creates a new ReviewDeletionException with the given message.
     *
     * @param message exception message
     */
    public ReviewDeletionException(String message) {
        super(message);
    }

    /**
     * Creates a new ReviewDeletionException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public ReviewDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}