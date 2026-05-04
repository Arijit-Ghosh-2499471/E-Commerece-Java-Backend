package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.mappers.CartMapper;
import com.cts.ecommerce.repository.OrderRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    CartMapper cartMapper = new CartMapper();

    @Override
    public int addOrder(Order order) {
        String sql1 = "INSERT INTO orders(userId,totalPrice,shippingAddressId,orderStatus,paymentStatus,shoppingCartId) VALUES(?,?,?,?,?,?)";
        String sql2 = "UPDATE ShoppingCart SET IsActive = FALSE WHERE ShoppingCartId = ?";
        jdbcTemplate.update(sql2, order.getShoppingCartId());
        return jdbcTemplate.update(sql1,
                order.getUserId(),
                order.getTotalPrice(),
                order.getShippingAddressId(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getShoppingCartId());
    }


    @Override
    public Order findById(int orderId) {
        String sql = "SELECT * FROM orders WHERE orderId = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Order.class), orderId);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE userId = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class), userId);
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public int updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET orderStatus = ? WHERE orderId = ?";
        return jdbcTemplate.update(sql, status, orderId);
    }

    @Override
    public int processPayment(int orderId, String paymentStatus) {
        String sql = "UPDATE orders SET paymentStatus = ? WHERE orderId = ?";
        return jdbcTemplate.update(sql, paymentStatus, orderId);
    }

    @Override
    public List<Map<String, Object>> getCartProducts(int userId) {
        String sql = """
                SELECT p.productId, p.productName, p.description, p.price, p.categoryId, p.imageURL, 
                       s.userId, s.shoppingCartId
                FROM Products p
                JOIN CartItems c ON p.ProductId = c.ProductId
                JOIN ShoppingCart s ON c.ShoppingCartId = s.ShoppingCartId
                WHERE s.UserId = ?;
                """;
        return jdbcTemplate.query(sql, cartMapper.getCartMapper(), userId);
    }

    @Override
    public double caluculateTotalPrice(int userId) {
        String sql = """
            SELECT SUM(p.Price * c.Quantity) AS TotalPrice
            FROM Products p
            JOIN CartItems c ON p.ProductId = c.ProductId
            JOIN ShoppingCart s ON c.ShoppingCartId = s.ShoppingCartId
            WHERE s.UserId = ?
            """;
        Double totalPrice = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Double.class);
        return totalPrice != null ? totalPrice : 0.0;
    }

    @Override
    public int getShoppingCartId(int userId) {
        String sql = "SELECT shoppingCartId FROM ShoppingCart WHERE userId = ? AND IsActive = TRUE";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
        return id == null ? -1 : id;
    }
}
