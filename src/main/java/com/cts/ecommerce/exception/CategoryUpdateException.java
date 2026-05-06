package com.cts.ecommerce.exception;

/**
 * Thrown when an existing {@link com.cts.ecommerce.entity.Category}
 * cannot be updated in the database.
 *
 * <p>Typical causes include the target category not existing, database
 * connectivity issues, or constraint violations on the updated values.</p>
 */
public class CategoryUpdateException extends RuntimeException {

    /**
     * Constructs a new {@code CategoryUpdateException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public CategoryUpdateException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CategoryUpdateException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public CategoryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
