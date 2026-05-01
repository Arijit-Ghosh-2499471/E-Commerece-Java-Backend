package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.CartItem;
import java.util.List;

public interface ShoppingCartService {

    void addProductToCart(int userId, int productId, int quantity);

    List<CartItem> viewActiveCart(int userId);

    void removeProductFromCart(int userId, int productId, int quantity);

    void checkout(int userId);
}
