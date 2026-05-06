package com.cts.ecommerce.exception;

/**
 * Thrown when a bulk retrieval or existence-check operation against
 * the {@code Category} table fails.
 *
 * <p>This covers operations such as {@code findAll} and
 * {@code existsById} where the failure is not tied to a specific
 * missing record but rather to an underlying query problem.</p>
 */
public class CategoryRetrievalException extends RuntimeException {

    /**
     * Constructs a new {@code CategoryRetrievalException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public CategoryRetrievalException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CategoryRetrievalException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public CategoryRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
