package com.cts.ecommerce.exception;

/**
 * Thrown when a bulk retrieval, search, or existence-check operation
 * against the {@code Products} table fails.
 *
 * <p>This covers operations such as {@code findAll},
 * {@code findByCategoryId}, {@code findByProductNameContaining},
 * {@code findByPriceBetween}, and {@code existsById} where the failure
 * is not tied to a specific missing record but rather to an underlying
 * query problem.</p>
 */
public class ProductRetrievalException extends RuntimeException {

    /**
     * Constructs a new {@code ProductRetrievalException} with the
     * specified detail message.
     *
     * @param message the detail message describing the cause of failure
     */
    public ProductRetrievalException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProductRetrievalException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing the cause of failure
     * @param cause   the underlying exception that triggered this error
     */
    public ProductRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}