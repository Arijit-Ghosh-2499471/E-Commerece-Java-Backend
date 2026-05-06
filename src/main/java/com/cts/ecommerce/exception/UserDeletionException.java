package com.cts.ecommerce.exception;

/**
 * Exception thrown when a user cannot be deleted.
 */
public class UserDeletionException extends RuntimeException {

    /**
     * Creates a new UserDeletionException with the given message.
     *
     * @param message exception message
     */
    public UserDeletionException(String message) {
        super(message);
    }

    /**
     * Creates a new UserDeletionException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public UserDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}