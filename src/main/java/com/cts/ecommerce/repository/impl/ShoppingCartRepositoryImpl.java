package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of {@link ShoppingCartRepository}.
 * Provides operations for managing {@link ShoppingCart} entities
 * using {@link JdbcTemplate}.
 */
@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private final JdbcTemplate jdbcTemplate;

    // SQL statements as constants

    /** SQL query to find an active shopping cart for a user */
    private static final String SELECT_ACTIVE_CART_SQL =
            "SELECT * FROM ShoppingCart WHERE UserId = ? AND IsActive = 1";

    /** SQL query to create a new shopping cart */
    private static final String INSERT_CART_SQL =
            "INSERT INTO ShoppingCart (UserId) VALUES (?)";

    /** SQL query to deactivate an existing shopping cart */
    private static final String DEACTIVATE_CART_SQL =
            "UPDATE ShoppingCart SET IsActive = false WHERE ShoppingCartId = ?";

    @Autowired
    public ShoppingCartRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves the active shopping cart for a given user.
     *
     * @param userId the user ID
     * @return the active {@link ShoppingCart}, or null if none exists
     */
    @Override
    public ShoppingCart findActiveCartByUserId(int userId) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_ACTIVE_CART_SQL,
                    new BeanPropertyRowMapper<>(ShoppingCart.class),
                    userId
            );
        } catch (Exception ex) {
            return null; // ✅ logic preserved
        }
    }

    /**
     * Creates a new shopping cart for the user.
     *
     * @param userId the user ID
     */
    @Override
    public void createCart(int userId) {
        try {
            jdbcTemplate.update(INSERT_CART_SQL, userId);
        } catch (Exception ex) {
            throw new ShoppingCartCreationException("Failed to create shopping cart for userId " + userId);
        }
    }

    /**
     * Deactivates an existing shopping cart.
     *
     * @param shoppingCartId the shopping cart ID
     */
    @Override
    public void deactivateCart(int shoppingCartId) {
        try {
            jdbcTemplate.update(DEACTIVATE_CART_SQL, shoppingCartId);
        } catch (Exception ex) {
            throw new ShoppingCartUpdateException("Failed to deactivate shopping cart with id " + shoppingCartId);
        }
    }
}