package com.cts.ecommerce.exception;

/**
 * Thrown when a requested resource (e.g., Product, Category) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
