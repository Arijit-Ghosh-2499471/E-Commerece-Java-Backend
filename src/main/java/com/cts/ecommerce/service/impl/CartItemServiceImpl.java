package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.CartItemRepository;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.CartItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link CartItemService} that manages cart item operations
 * for active {@link ShoppingCart} entities.
 * <p>
 * This service validates shopping cart existence, handles quantity updates,
 * and ensures meaningful custom exceptions are thrown on failure.
 */
@Service
public class CartItemServiceImpl implements CartItemService {

    private static final Logger logger =
            LoggerFactory.getLogger(CartItemServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(ShoppingCartRepository shoppingCartRepository,
                               CartItemRepository cartItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Adds an item to the user's active shopping cart.
     * <p>
     * If the item already exists in the cart, its quantity is increased.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @param quantity  the quantity to add
     * @throws ShoppingCartNotFoundException if active cart is not found
     * @throws CartOperationException        if the operation fails
     */
    @Override
    public void addItem(int userId, int productId, int quantity) {
        logger.info("Attempting to add item | UserId={} ProductId={} Quantity={}",
                userId, productId, quantity);

        try {
            ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);
            if (cart == null) {
                throw new ShoppingCartNotFoundException(
                        "Active cart does not exist for userId " + userId);
            }

            int cartId = cart.getShoppingCartId();
            CartItem item = cartItemRepository.getItem(cartId, productId);

            if (item == null) {
                cartItemRepository.addItem(cartId, productId, quantity);
                logger.info("Item successfully added to cart | CartId={} ProductId={}",
                        cartId, productId);
            } else {
                int updatedQuantity = item.getQuantity() + quantity;
                cartItemRepository.updateQuantity(cartId, productId, updatedQuantity);
                logger.info("Item quantity updated | CartId={} ProductId={} NewQuantity={}",
                        cartId, productId, updatedQuantity);
            }
        } catch (ShoppingCartNotFoundException ex) {
            logger.error("Add item failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while adding item to cart", ex);
            throw new CartOperationException("Error occurred while adding item to cart");
        }
    }

    /**
     * Removes or reduces quantity of an item from the user's active cart.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @param quantity  the quantity to remove
     * @throws ShoppingCartNotFoundException if active cart is not found
     * @throws CartItemNotFoundException     if item does not exist in cart
     * @throws CartOperationException        if the operation fails
     */
    @Override
    public void removeItem(int userId, int productId, int quantity) {
        logger.info("Attempting to remove item | UserId={} ProductId={}", userId, productId);

        try {
            ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);
            if (cart == null) {
                throw new ShoppingCartNotFoundException(
                        "Active cart not found for userId " + userId);
            }

            int cartId = cart.getShoppingCartId();
            CartItem item = cartItemRepository.getItem(cartId, productId);

            if (item == null) {
                throw new CartItemNotFoundException(
                        "Item not found in cart for productId " + productId);
            }

            int remainingQuantity = item.getQuantity() - quantity;

            if (remainingQuantity <= 0) {
                cartItemRepository.removeItem(cartId, productId);
                logger.info("Item removed from cart | CartId={} ProductId={}", cartId, productId);
            } else {
                cartItemRepository.updateQuantity(cartId, productId, remainingQuantity);
                logger.info("Item quantity reduced | CartId={} ProductId={} RemainingQuantity={}",
                        cartId, productId, remainingQuantity);
            }
        } catch (ShoppingCartNotFoundException | CartItemNotFoundException ex) {
            logger.warn("Remove item skipped: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while removing item from cart", ex);
            throw new CartOperationException("Error occurred while removing item from cart");
        }
    }

    /**
     * Retrieves all items in the user's active shopping cart.
     *
     * @param userId the user ID
     * @return list of {@link CartItem} entities, or empty list if no active cart
     * @throws CartOperationException if retrieval fails
     */
    @Override
    public List<CartItem> viewActiveCart(int userId) {
        logger.debug("Fetching active cart items for userId={}", userId);

        try {
            ShoppingCart cart = shoppingCartRepository.findActiveCartByUserId(userId);
            if (cart == null) {
                logger.info("No active cart found for userId={}", userId);
                return List.of();
            }
            return cartItemRepository.getItemsByCartId(cart.getShoppingCartId());
        } catch (Exception ex) {
            logger.error("Error while viewing active cart for userId={}", userId, ex);
            throw new CartOperationException("Error occurred while viewing active cart");
        }
    }

    /**
     * Retrieves all cart items using the shopping cart ID.
     *
     * @param shoppingCartId the shopping cart ID
     * @return list of {@link CartItem} entities
     * @throws CartOperationException if retrieval fails
     */
    @Override
    public List<CartItem> getItemsByShoppingCartId(int shoppingCartId) {
        logger.debug("Fetching cart items for shoppingCartId={}", shoppingCartId);

        try {
            return cartItemRepository.getItemsByCartId(shoppingCartId);
        } catch (Exception ex) {
            logger.error("Error fetching items for shoppingCartId={}", shoppingCartId, ex);
            throw new CartOperationException("Error fetching cart items");
        }
    }
}