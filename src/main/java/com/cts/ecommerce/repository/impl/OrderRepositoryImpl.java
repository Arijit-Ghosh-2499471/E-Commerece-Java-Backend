package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.mappers.CartMapper;
import com.cts.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * JDBC-based implementation of {@link OrderRepository}.
 * Provides CRUD operations, payment processing, and shopping cart queries
 * for {@link Order} entities using {@link JdbcTemplate}.
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final CartMapper cartMapper = new CartMapper();

    /** SQL query to insert a new order */
    private static final String SQL_INSERT_ORDER =
            "INSERT INTO Orders (userId, totalPrice, shippingAddress, orderStatus, paymentStatus, shoppingCartId) VALUES (?, ?, ?, ?, ?, ?)";

    /** SQL query to find an order by ID */
    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM Orders WHERE orderId = ?";

    /** SQL query to find orders by user ID */
    private static final String SQL_FIND_BY_USER =
            "SELECT * FROM Orders WHERE userId = ?";

    /** SQL query to retrieve all orders */
    private static final String SQL_FIND_ALL =
            "SELECT * FROM Orders";

    /** SQL query to update the status of an order */
    private static final String SQL_UPDATE_ORDER_STATUS =
            "UPDATE Orders SET orderStatus = ? WHERE orderId = ?";

    /** SQL query to update the payment status of an order */
    private static final String SQL_UPDATE_PAYMENT_STATUS =
            "UPDATE Orders SET paymentStatus = ? WHERE orderId = ?";

    /** SQL query to retrieve products in a user's cart */
    private static final String SQL_GET_CART_PRODUCTS = """
            SELECT p.productId, p.productName, p.description, p.price, p.categoryId, p.imageURL,
                   s.userId, s.shoppingCartId
            FROM Products p
            JOIN CartItems c ON p.ProductId = c.ProductId
            JOIN ShoppingCart s ON c.ShoppingCartId = s.ShoppingCartId
            WHERE s.UserId = ?
            """;

    /** SQL query to calculate total price of products in a user's cart */
    private static final String SQL_CALCULATE_TOTAL_PRICE = """
            SELECT SUM(p.Price * c.Quantity) AS TotalPrice
            FROM Products p
            JOIN CartItems c ON p.ProductId = c.ProductId
            JOIN ShoppingCart s ON c.ShoppingCartId = s.ShoppingCartId
            WHERE s.UserId = ?
            """;

    /** SQL query to get the shopping cart ID for a user */
    private static final String SQL_GET_CART_ID =
            "SELECT shoppingCartId FROM ShoppingCart WHERE userId = ? AND IsActive = TRUE";

    /**
     * Saves a new order to the database.
     *
     * @param order the {@link Order} entity to be saved
     * @return number of rows affected (0 if insert failed)
     */
    @Override
    public int addOrder(Order order) {
        try {
            return jdbcTemplate.update(SQL_INSERT_ORDER,
                    order.getUserId(),
                    order.getTotalPrice(),
                    order.getShippingAddressId(),
                    order.getOrderStatus(),
                    order.getPaymentStatus(),
                    order.getShoppingCartId());
        } catch (Exception ex) {
            throw new OrderCreationException("Failed to create order");
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order
     * @return the {@link Order} entity if found
     */
    @Override
    public Order findById(int orderId) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,
                    new BeanPropertyRowMapper<>(Order.class), orderId);
        } catch (Exception ex) {
            throw new OrderNotFoundException("Order not found with id " + orderId);
        }
    }

    /**
     * Retrieves all orders associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Order} entities for the user
     */
    @Override
    public List<Order> findOrdersByUserId(int userId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_USER,
                    new BeanPropertyRowMapper<>(Order.class), userId);
        } catch (Exception ex) {
            throw new OrderNotFoundException("Orders not found for userId " + userId);
        }
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return list of all {@link Order} entities
     */
    @Override
    public List<Order> findAll() {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL,
                    new BeanPropertyRowMapper<>(Order.class));
        } catch (Exception ex) {
            throw new OrderNotFoundException("Failed to fetch orders");
        }
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId the ID of the order
     * @param status  the new status value
     * @return number of rows affected (0 if update failed)
     */
    @Override
    public int updateOrderStatus(int orderId, String status) {
        try {
            return jdbcTemplate.update(SQL_UPDATE_ORDER_STATUS, status, orderId);
        } catch (Exception ex) {
            throw new OrderUpdateException("Failed to update order status");
        }
    }

    /**
     * Updates the payment status of an order.
     *
     * @param orderId       the ID of the order
     * @param paymentStatus the new payment status
     * @return number of rows affected (0 if update failed)
     */
    @Override
    public int processPayment(int orderId, String paymentStatus) {
        try {
            return jdbcTemplate.update(SQL_UPDATE_PAYMENT_STATUS, paymentStatus, orderId);
        } catch (Exception ex) {
            throw new PaymentProcessingException("Payment processing failed");
        }
    }

    /**
     * Retrieves products currently in the user's shopping cart.
     *
     * @param userId the ID of the user
     * @return list of cart products represented as maps of field values
     */
    @Override
    public List<Map<String, Object>> getCartProducts(int userId) {
        try {
            return jdbcTemplate.query(SQL_GET_CART_PRODUCTS, cartMapper.getCartMapper(), userId);
        } catch (Exception ex) {
            throw new ShoppingCartNotFoundException("Cart products not found for userId " + userId);
        }
    }

    /**
     * Calculates the total price of products in the user's shopping cart.
     *
     * @param userId the ID of the user
     * @return total price of cart items
     */
    @Override
    public double calculateTotalPrice(int userId) {
        try {
            Double totalPrice = jdbcTemplate.queryForObject(SQL_CALCULATE_TOTAL_PRICE,
                    new Object[]{userId}, Double.class);
            return totalPrice != null ? totalPrice : 0.0;
        } catch (Exception ex) {
            throw new ShoppingCartNotFoundException("Failed to calculate total price");
        }
    }

    /**
     * Retrieves the shopping cart ID for a given user.
     *
     * @param userId the ID of the user
     * @return shopping cart ID, or -1 if none exists
     */
    @Override
    public int getShoppingCartId(int userId) {
        try {
            Integer id = jdbcTemplate.queryForObject(SQL_GET_CART_ID,
                    new Object[]{userId}, Integer.class);
            return id == null ? -1 : id;
        } catch (Exception ex) {
            throw new ShoppingCartNotFoundException("Active shopping cart not found for userId " + userId);
        }
    }
}