package com.cts.ecommerce.exception;

/**
 * Thrown when a {@link com.cts.ecommerce.entity.Product} cannot be
 * deleted from the database.
 *
 * <p>Typical causes include foreign-key constraint violations (e.g.
 * the product is referenced by an existing cart or order), the row
 * not existing, or database connectivity issues.</p>
 */
public class ProductDeletionException extends RuntimeException {

    /**
     * Constructs a new {@code ProductDeletionException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public ProductDeletionException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProductDeletionException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public ProductDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}