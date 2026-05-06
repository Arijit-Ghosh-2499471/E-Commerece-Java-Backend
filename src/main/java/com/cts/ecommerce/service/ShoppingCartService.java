package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.ShoppingCart;

public interface ShoppingCartService {
    void checkout(int userId);

    ShoppingCart findActiveCardByUserId(int userId);
}