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

    private final CartMapper cartMapper = new CartMapper();

    // SQL constants
    private static final String SQL_INSERT_ORDER = "INSERT INTO Orders (userId, totalPrice, shippingAddress, orderStatus, paymentStatus, shoppingCartId) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM Orders WHERE orderId = ?";

    private static final String SQL_FIND_BY_USER = "SELECT * FROM Orders WHERE userId = ?";

    private static final String SQL_FIND_ALL = "SELECT * FROM Orders";

    private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE Orders SET orderStatus = ? WHERE orderId = ?";

    private static final String SQL_UPDATE_PAYMENT_STATUS = "UPDATE Orders SET paymentStatus = ? WHERE orderId = ?";

    private static final String SQL_GET_CART_PRODUCTS = """
            SELECT p.productId, p.productName, p.description, p.price, p.categoryId, p.imageURL, 
                   s.userId, s.shoppingCartId
            FROM Products p
            JOIN CartItems c ON p.ProductId = c.ProductId
            JOIN ShoppingCart s ON c.ShoppingCartId = s.ShoppingCartId
            WHERE s.UserId = ?
            """;

    private static final String SQL_CALCULATE_TOTAL_PRICE = """
            SELECT SUM(p.Price * c.Quantity) AS TotalPrice
            FROM Products p
            JOIN CartItems c ON p.ProductId = c.ProductId
            JOIN ShoppingCart s ON c.ShoppingCartId = s.ShoppingCartId
            WHERE s.UserId = ?
            """;

    private static final String SQL_GET_CART_ID = "SELECT shoppingCartId FROM ShoppingCart WHERE userId = ? AND IsActive = TRUE";

    @Override
    public int addOrder(Order order) {
        return jdbcTemplate.update(SQL_INSERT_ORDER,
                order.getUserId(),
                order.getTotalPrice(),
                order.getShippingAddressId(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getShoppingCartId());
    }

    @Override
    public Order findById(int orderId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,
                new BeanPropertyRowMapper<>(Order.class), orderId);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        return jdbcTemplate.query(SQL_FIND_BY_USER,
                new BeanPropertyRowMapper<>(Order.class), userId);
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL,
                new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public int updateOrderStatus(int orderId, String status) {
        return jdbcTemplate.update(SQL_UPDATE_ORDER_STATUS, status, orderId);
    }

    @Override
    public int processPayment(int orderId, String paymentStatus) {
        return jdbcTemplate.update(SQL_UPDATE_PAYMENT_STATUS, paymentStatus, orderId);
    }

    @Override
    public List<Map<String, Object>> getCartProducts(int userId) {
        return jdbcTemplate.query(SQL_GET_CART_PRODUCTS, cartMapper.getCartMapper(), userId);
    }

    @Override
    public double calculateTotalPrice(int userId) {
        Double totalPrice = jdbcTemplate.queryForObject(SQL_CALCULATE_TOTAL_PRICE,
                new Object[]{userId}, Double.class);
        return totalPrice != null ? totalPrice : 0.0;
    }

    @Override
    public int getShoppingCartId(int userId) {
        Integer id = jdbcTemplate.queryForObject(SQL_GET_CART_ID,
                new Object[]{userId}, Integer.class);
        return id == null ? -1 : id;
    }
}
