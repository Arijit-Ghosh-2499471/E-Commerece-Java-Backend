package com.cts.ecommerce.exception;

/**
 * Thrown when a {@link com.cts.ecommerce.entity.Product} cannot be
 * persisted to the database during a save (insert) operation.
 *
 * <p>Typical causes include database connectivity issues, constraint
 * violations (e.g. invalid {@code CategoryId} foreign key), or invalid
 * data passed to the repository layer.</p>
 */
public class ProductCreationException extends RuntimeException {

    /**
     * Constructs a new {@code ProductCreationException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public ProductCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProductCreationException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}