package com.cts.ecommerce.exception;

public class CartOperationException extends RuntimeException {
    public CartOperationException(String message) {
        super(message);
    }
}