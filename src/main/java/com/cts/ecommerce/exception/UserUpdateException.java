package com.cts.ecommerce.exception;

/**
 * Exception thrown when user details cannot be updated.
 */
public class UserUpdateException extends RuntimeException {

    /**
     * Creates a new UserUpdateException with the given message.
     *
     * @param message exception message
     */
    public UserUpdateException(String message) {
        super(message);
    }

    /**
     * Creates a new UserUpdateException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}