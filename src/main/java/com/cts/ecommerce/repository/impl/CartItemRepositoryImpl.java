package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.exception.CartItemCreationException;
import com.cts.ecommerce.exception.CartItemDeletionException;
import com.cts.ecommerce.exception.CartItemNotFoundException;
import com.cts.ecommerce.exception.CartItemUpdateException;
import com.cts.ecommerce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of {@link CartItemRepository}.
 * Provides CRUD operations for {@link CartItem} entities
 * using {@link JdbcTemplate}.
 */
@Repository
public class CartItemRepositoryImpl implements CartItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CartItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** SQL query to insert a new item into the cart */
    private static final String INSERT_ITEM_SQL =
            "INSERT INTO CartItems (ShoppingCartId, ProductId, Quantity) VALUES (?, ?, ?)";

    /** SQL query to update quantity of an existing cart item */
    private static final String UPDATE_QUANTITY_SQL =
            "UPDATE CartItems SET Quantity = ? WHERE ShoppingCartId = ? AND ProductId = ?";

    /** SQL query to retrieve a specific cart item */
    private static final String SELECT_ITEM_SQL =
            "SELECT * FROM CartItems WHERE ShoppingCartId = ? AND ProductId = ?";

    /** SQL query to retrieve all items for a shopping cart */
    private static final String SELECT_ITEMS_BY_CART_SQL =
            "SELECT * FROM CartItems WHERE ShoppingCartId = ?";

    /** SQL query to delete a cart item */
    private static final String DELETE_ITEM_SQL =
            "DELETE FROM CartItems WHERE ShoppingCartId = ? AND ProductId = ?";

    /**
     * Adds a new item to the shopping cart.
     *
     * @param shoppingCartId the shopping cart ID
     * @param productId the product ID
     * @param quantity the quantity of the product
     */
    @Override
    public void addItem(int shoppingCartId, int productId, int quantity) {
        try {
            jdbcTemplate.update(INSERT_ITEM_SQL, shoppingCartId, productId, quantity);
        } catch (Exception ex) {
            throw new CartItemCreationException("Failed to add item to cart");
        }
    }

    /**
     * Updates the quantity of an item in the shopping cart.
     *
     * @param shoppingCartId the shopping cart ID
     * @param productId the product ID
     * @param quantity the updated quantity
     */
    @Override
    public void updateQuantity(int shoppingCartId, int productId, int quantity) {
        try {
            jdbcTemplate.update(UPDATE_QUANTITY_SQL, quantity, shoppingCartId, productId);
        } catch (Exception ex) {
            throw new CartItemUpdateException("Failed to update cart item quantity");
        }
    }

    /**
     * Retrieves a specific cart item by cart ID and product ID.
     *
     * @param shoppingCartId the shopping cart ID
     * @param productId the product ID
     * @return the {@link CartItem} if found, otherwise null
     */
    @Override
    public CartItem getItem(int shoppingCartId, int productId) {
        try {
            List<CartItem> items = jdbcTemplate.query(
                    SELECT_ITEM_SQL,
                    new BeanPropertyRowMapper<>(CartItem.class),
                    shoppingCartId,
                    productId
            );
            return items.isEmpty() ? null : items.getFirst();
        } catch (Exception ex) {
            throw new CartItemNotFoundException("Cart item not found");
        }
    }

    /**
     * Retrieves all cart items for a specific shopping cart.
     *
     * @param shoppingCartId the shopping cart ID
     * @return list of {@link CartItem} entities
     */
    @Override
    public List<CartItem> getItemsByCartId(int shoppingCartId) {
        try {
            return jdbcTemplate.query(
                    SELECT_ITEMS_BY_CART_SQL,
                    new BeanPropertyRowMapper<>(CartItem.class),
                    shoppingCartId
            );
        } catch (Exception ex) {
            throw new CartItemNotFoundException("Cart items not found for cartId " + shoppingCartId);
        }
    }

    /**
     * Removes an item from the shopping cart.
     *
     * @param shoppingCartId the shopping cart ID
     * @param productId the product ID
     */
    @Override
    public void removeItem(int shoppingCartId, int productId) {
        try {
            jdbcTemplate.update(DELETE_ITEM_SQL, shoppingCartId, productId);
        } catch (Exception ex) {
            throw new CartItemDeletionException("Failed to remove item from cart");
        }
    }
}