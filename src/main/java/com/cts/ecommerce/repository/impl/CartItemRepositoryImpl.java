package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.repository.CartItemRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemRepositoryImpl implements CartItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addItem(int shoppingCartId, int productId, int quantity) {
        String sql = "INSERT INTO CartItems (ShoppingCartId, ProductId, Quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, shoppingCartId, productId, quantity);
    }

    @Override
    public void updateQuantity(int shoppingCartId, int productId, int quantity) {
        String sql = "UPDATE CartItems SET Quantity = ? WHERE ShoppingCartId = ? AND ProductId = ?";
        jdbcTemplate.update(sql, quantity, shoppingCartId, productId);
    }

    @Override
    public CartItem getItem(int shoppingCartId, int productId) {
        String sql = "SELECT * FROM CartItems WHERE ShoppingCartId = ? AND ProductId = ?";
        List<CartItem> items = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CartItem.class),
                shoppingCartId,
                productId
        );

        return items.isEmpty() ? null : items.getFirst();
    }

    @Override
    public List<CartItem> getItemsByCartId(int shoppingCartId) {
        String sql = "SELECT * FROM CartItems WHERE ShoppingCartId = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CartItem.class), shoppingCartId);
    }

    @Override
    public void removeItem(int shoppingCartId, int productId) {
        String sql = "DELETE FROM CartItems WHERE ShoppingCartId = ? AND ProductId = ?";
        jdbcTemplate.update(sql, shoppingCartId, productId);
    }


}