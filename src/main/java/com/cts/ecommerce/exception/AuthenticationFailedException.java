package com.cts.ecommerce.exception;

/**
 * Exception thrown when login authentication fails.
 */
public class AuthenticationFailedException extends RuntimeException {

    /**
     * Creates a new AuthenticationFailedException with the given message.
     *
     * @param message exception message
     */
    public AuthenticationFailedException(String message) {
        super(message);
    }

    /**
     * Creates a new AuthenticationFailedException with the given message and cause.
     *
     * @param message exception message
     * @param cause original exception cause
     */
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}