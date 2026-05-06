package com.cts.ecommerce.exception;

/**
 * Exception thrown when a user cannot be created or registered.
 */
public class UserCreationException extends RuntimeException {

    /**
     * Creates a new UserCreationException with the given message.
     *
     * @param message exception message
     */
    public UserCreationException(String message) {
        super(message);
    }

    /**
     * Creates a new UserCreationException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}