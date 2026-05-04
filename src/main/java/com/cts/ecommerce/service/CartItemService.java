package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.CartItem;

import java.util.List;

public interface CartItemService {

    void addItem(int userId, int productId, int quantity);

    void removeItem(int userId, int productId, int quantity);

    List<CartItem> viewActiveCart(int userId);
}