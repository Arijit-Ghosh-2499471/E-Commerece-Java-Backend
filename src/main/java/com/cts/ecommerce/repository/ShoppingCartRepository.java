package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.ShoppingCart;

public interface ShoppingCartRepository {

    ShoppingCart findActiveCartByUserId(int userId);

    void createCart(int userId);

    void deactivateCart(int shoppingCartId);

}
