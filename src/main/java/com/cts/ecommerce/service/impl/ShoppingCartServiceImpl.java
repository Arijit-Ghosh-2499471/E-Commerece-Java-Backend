package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ShoppingCartService} that manages
 * shopping cart lifecycle operations.
 * <p>
 * This service handles checkout flow, active cart retrieval,
 * and ensures meaningful exceptions are thrown for failure scenarios.
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger logger =
            LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * Performs checkout for the user's active shopping cart.
     * <p>
     * The active cart is deactivated and a new cart is created.
     *
     * @param userId the user ID
     * @throws CartOperationException if checkout processing fails
     */
    @Override
    public void checkout(int userId) {
        logger.info("Initiating checkout for userId={}", userId);

        try {
            ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);

            if (cart == null) {
                throw new ShoppingCartNotFoundException(
                        "No active cart found for checkout for userId " + userId);
            }

            shoppingCartRepository.deactivateCart(cart.getShoppingCartId());
            shoppingCartRepository.createCart(userId);

            logger.info("Checkout successful | UserId={} ClosedCartId={}",
                    userId, cart.getShoppingCartId());

        } catch (ShoppingCartNotFoundException ex) {
            logger.warn("Checkout skipped: {}", ex.getMessage());
        } catch (Exception ex) {
            logger.error("Checkout failed for userId={}", userId, ex);
            throw new CartOperationException("Checkout failed");
        }
    }

    /**
     * Retrieves the active shopping cart for a user.
     *
     * @param userId the user ID
     * @return the active {@link ShoppingCart}, or null if none exists
     * @throws CartOperationException if retrieval fails
     */
    @Override
    public ShoppingCart findActiveCardByUserId(int userId) {
        logger.debug("Fetching active cart for userId={}", userId);

        try {
            return shoppingCartRepository.findActiveCartByUserId(userId);
        } catch (Exception ex) {
            logger.error("Error fetching active cart for userId={}", userId, ex);
            throw new CartOperationException("Error fetching active cart");
        }
    }
}