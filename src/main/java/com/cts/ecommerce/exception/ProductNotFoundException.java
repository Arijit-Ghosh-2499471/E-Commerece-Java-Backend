package com.cts.ecommerce.exception;

/**
 * Thrown when a {@link com.cts.ecommerce.entity.Product} cannot be
 * located in the database for a given identifier.
 *
 * <p>This exception is raised by single-record lookup operations such
 * as {@code findById} when no matching row exists, or when the
 * underlying query fails for any other reason.</p>
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ProductNotFoundException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProductNotFoundException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}