package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.repository.CartItemRepository;
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

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("CartItemId"));
                item.setShoppingCartId(rs.getInt("ShoppingCartId"));
                item.setProductId(rs.getInt("ProductId"));
                item.setQuantity(rs.getInt("Quantity"));
                return item;
            }, shoppingCartId, productId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CartItem> getItemsByCartId(int shoppingCartId) {

        String sql = "SELECT * FROM cartitems WHERE ShoppingCartId = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CartItem item = new CartItem();
            item.setCartItemId(rs.getInt("CartItemId"));
            item.setShoppingCartId(rs.getInt("ShoppingCartId"));
            item.setProductId(rs.getInt("ProductId"));
            item.setQuantity(rs.getInt("Quantity"));
            return item;
        }, shoppingCartId);
    }

    @Override
    public void removeItem(int shoppingCartId, int productId) {

        String sql = "DELETE FROM CartItems WHERE ShoppingCartId = ? AND ProductId = ?";

        jdbcTemplate.update(sql, shoppingCartId, productId);
    }
}
