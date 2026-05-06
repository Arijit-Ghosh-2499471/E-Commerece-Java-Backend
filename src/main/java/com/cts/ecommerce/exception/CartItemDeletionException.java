package com.cts.ecommerce.exception;

public class CartItemDeletionException extends RuntimeException {
    public CartItemDeletionException(String message) {
        super(message);
    }
}
