package com.cts.ecommerce.exception;

/**
 * Thrown when the service layer receives input that fails validation
 * before any persistence operation is attempted.
 *
 * <p>Typical causes include {@code null} or blank required fields,
 * negative numeric values where positives are required, or a price
 * range whose lower bound exceeds its upper bound. This exception
 * indicates a client-side error rather than a database failure.</p>
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidInputException} with the
     * specified detail message.
     *
     * @param message the detail message describing why the input was
     *                considered invalid
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidInputException} with the
     * specified detail message and underlying cause.
     *
     * @param message the detail message describing why the input was
     *                considered invalid
     * @param cause   the underlying exception that triggered this error
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}