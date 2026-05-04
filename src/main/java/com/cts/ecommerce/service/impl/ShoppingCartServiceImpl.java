package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void checkout(int userId) {

        ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            return;
        }

        shoppingCartRepository.deactivateCart(cart.getShoppingCartId());

        shoppingCartRepository.createCart(userId);
    }
}