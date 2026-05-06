package com.cts.ecommerce.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Creates a new UserNotFoundException with the given message.
     *
     * @param message exception message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new UserNotFoundException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}