package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.repository.CartItemRepository;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    public ShoppingCartServiceImpl(
            ShoppingCartRepository shoppingCartRepository,
            CartItemRepository cartItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void addProductToCart(int userId, int productId, int quantity) {

        ShoppingCart cart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        int cartId = cart.getShoppingCartId();

        CartItem existingItem =
                cartItemRepository.getItem(cartId, productId);

        if (existingItem == null) {
            cartItemRepository.addItem(cartId, productId, quantity);
        } else {
            int updatedQuantity =
                    existingItem.getQuantity() + quantity;
            cartItemRepository.updateQuantity(
                    cartId, productId, updatedQuantity);
        }
    }

    @Override
    public List<CartItem> viewActiveCart(int userId) {

        ShoppingCart cart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            return List.of();
        }

        return cartItemRepository
                .getItemsByCartId(cart.getShoppingCartId());
    }

    @Override
    public void removeProductFromCart(int userId, int productId, int quantity) {

        ShoppingCart cart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            return;
        }

        int cartId = cart.getShoppingCartId();

        CartItem existingItem =
                cartItemRepository.getItem(cartId, productId);

        if (existingItem == null) {
            return;
        }

        int remainingQuantity =
                existingItem.getQuantity() - quantity;

        if (remainingQuantity <= 0) {
            cartItemRepository.removeItem(cartId, productId);
        } else {
            cartItemRepository.updateQuantity(
                    cartId, productId, remainingQuantity);
        }
    }

    @Override
    public void checkout(int userId) {

        ShoppingCart cart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            return;
        }

        shoppingCartRepository
                .deactivateCart(cart.getShoppingCartId());

        shoppingCartRepository.createCart(userId);
    }
}
