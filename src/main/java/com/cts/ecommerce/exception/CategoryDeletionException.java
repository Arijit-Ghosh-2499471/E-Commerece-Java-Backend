package com.cts.ecommerce.exception;

/**
 * Thrown when a {@link com.cts.ecommerce.entity.Category} cannot be
 * deleted from the database.
 *
 * <p>Typical causes include foreign-key constraint violations (e.g.
 * the category still has products referencing it), the row not
 * existing, or database connectivity issues.</p>
 */
public class CategoryDeletionException extends RuntimeException {

    /**
     * Constructs a new {@code CategoryDeletionException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public CategoryDeletionException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CategoryDeletionException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public CategoryDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}