package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.repository.CartItemRepository;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(ShoppingCartRepository shoppingCartRepository,
                               CartItemRepository cartItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

        ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            throw new IllegalStateException("Active cart does not exist for user " + userId);
        }

        int cartId = cart.getShoppingCartId();

        CartItem item = cartItemRepository.getItem(cartId, productId);

        if (item == null) {
            cartItemRepository.addItem(cartId, productId, quantity);
        } else {
            cartItemRepository.updateQuantity(cartId, productId, item.getQuantity() + quantity);
        }
    }

    @Override
    public void removeItem(int userId, int productId, int quantity) {

        ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) return;

        int cartId = cart.getShoppingCartId();

        CartItem item = cartItemRepository.getItem(cartId, productId);

        if (item == null) return;

        int remainingQuantity = item.getQuantity() - quantity;

        if (remainingQuantity <= 0) {
            cartItemRepository.removeItem(cartId, productId);
        } else {
            cartItemRepository.updateQuantity(cartId, productId, remainingQuantity);
        }
    }

    @Override
    public List<CartItem> viewActiveCart(int userId) {

        ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            return List.of();
        }

        return cartItemRepository.getItemsByCartId(cart.getShoppingCartId());
    }
}