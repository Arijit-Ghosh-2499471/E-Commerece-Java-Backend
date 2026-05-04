package com.cts.ecommerce.repository;


import com.cts.ecommerce.entity.CartItem;

import java.util.List;

public interface CartItemRepository {

    void addItem(int shoppingCartId, int productId, int quantity);

    void updateQuantity(int shoppingCartId, int productId, int quantity);

    CartItem getItem(int shoppingCartId, int productId);

    List<CartItem> getItemsByCartId(int shoppingCartId);

    void removeItem(int shoppingCartId, int productId);
}

