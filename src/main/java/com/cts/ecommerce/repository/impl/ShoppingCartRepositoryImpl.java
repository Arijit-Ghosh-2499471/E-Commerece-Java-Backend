package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartRepositoryImpl implements ShoppingCartRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ShoppingCartRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ShoppingCart findActiveCartByUserId(int userId) {
        String sql = "SELECT * FROM ShoppingCart WHERE UserId = ? AND IsActive = 1";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rn) -> {
                ShoppingCart cart = new ShoppingCart();
                cart.setShoppingCartId(rs.getInt("ShoppingCartId"));
                cart.setUserId(rs.getInt("UserId"));
                cart.setActive(rs.getBoolean("IsActive"));
                return cart;
            }, userId);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void createCart(int userId) {
        String sql = "INSERT INTO ShoppingCart (UserId) VALUES (?)";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void deactivateCart(int shoppingCartId) {
        String sql = "UPDATE ShoppingCart SET IsActive = false WHERE ShoppingCartId = ?";
        jdbcTemplate.update(sql, shoppingCartId);

    }
}
