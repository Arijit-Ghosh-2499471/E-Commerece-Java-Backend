package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.OrderRepository;
import com.cts.ecommerce.service.OrderService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link OrderService} that provides CRUD operations
 * and business logic for {@link Order} entities using a JDBC-based
 * {@link OrderRepository}.
 * <p>
 * Each method validates repository results and throws custom exceptions
 * when operations fail, ensuring robust error handling.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Adds a new order to the database.
     *
     * @param order the {@link Order} entity to be saved
     * @throws OrderCreationException if the insert fails
     */
    @Override
    public void addOrder(Order order) {
        logger.info("Attempting to add order for userId={}", order.getUserId());
        int rows = orderRepository.addOrder(order);
        if (rows == 0) {
            logger.error("Failed to create order for userId={}", order.getUserId());
            throw new OrderCreationException("Failed to create order for userId: " + order.getUserId());
        }
        logger.info("Successfully created order for userId={}", order.getUserId());
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order
     * @return the {@link Order} entity
     * @throws OrderNotFoundException if no order exists
     */
    @Override
    public Order findById(int orderId) {
        logger.debug("Fetching orderId={}", orderId);
        try {
            Order order = orderRepository.findById(orderId);
            logger.info("Found orderId={}", orderId);
            return order;
        } catch (EmptyResultDataAccessException ex) {
            logger.warn("Order not found with id={}", orderId);
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
    }

    /**
     * Retrieves all orders for a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Order} entities
     * @throws OrderNotFoundException if no orders exist
     */
    @Override
    public List<Order> findOrdersByUserId(int userId) {
        logger.debug("Fetching orders for userId={}", userId);
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            logger.warn("No orders found for userId={}", userId);
            throw new OrderNotFoundException("No orders found for userId: " + userId);
        }
        logger.info("Found {} orders for userId={}", orders.size(), userId);
        return orders;
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return list of all {@link Order} entities
     * @throws OrderNotFoundException if no orders exist
     */
    @Override
    public List<Order> findAll() {
        logger.debug("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        if (orders == null || orders.isEmpty()) {
            logger.warn("No orders found in the system");
            throw new OrderNotFoundException("No orders found in the system");
        }
        logger.info("Found {} orders in the system", orders.size());
        return orders;
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId the ID of the order
     * @param status  the new status
     * @return number of rows affected
     * @throws OrderUpdateException if update fails
     */
    @Override
    public int updateOrderStatus(int orderId, String status) {
        logger.info("Updating status of orderId={} to {}", orderId, status);
        int rows = orderRepository.updateOrderStatus(orderId, status);
        if (rows == 0) {
            logger.error("Failed to update status for orderId={}", orderId);
            throw new OrderUpdateException("Failed to update status for orderId: " + orderId);
        }
        logger.info("Successfully updated status for orderId={}", orderId);
        return rows;
    }

    /**
     * Processes payment for an order.
     *
     * @param orderId       the ID of the order
     * @param paymentStatus the new payment status
     * @return number of rows affected
     * @throws PaymentProcessingException if payment update fails
     */
    @Override
    public int processPayment(int orderId, String paymentStatus) {
        logger.info("Processing payment for orderId={} with status={}", orderId, paymentStatus);
        int rows = orderRepository.processPayment(orderId, paymentStatus);
        if (rows == 0) {
            logger.error("Failed to process payment for orderId={}", orderId);
            throw new PaymentProcessingException("Failed to process payment for orderId: " + orderId);
        }
        logger.info("Successfully processed payment for orderId={}", orderId);
        return rows;
    }

    /**
     * Retrieves products in the user's cart.
     *
     * @param userId the ID of the user
     * @return list of cart products
     * @throws OrderNotFoundException if no products exist
     */
    @Override
    public List<Map<String, Object>> getCartProducts(int userId) {
        logger.debug("Fetching cart products for userId={}", userId);
        List<Map<String, Object>> products = orderRepository.getCartProducts(userId);
        if (products == null || products.isEmpty()) {
            logger.warn("No cart products found for userId={}", userId);
            throw new OrderNotFoundException("No cart products found for userId: " + userId);
        }
        logger.info("Found {} cart products for userId={}", products.size(), userId);
        return products;
    }

    /**
     * Calculates the total price of products in the user's cart.
     *
     * @param userId the ID of the user
     * @return total price
     * @throws OrderNotFoundException if no products exist
     */
    @Override
    public double calculateTotalPrice(int userId) {
        logger.debug("Calculating total price for userId={}", userId);
        double total = orderRepository.calculateTotalPrice(userId);
        if (total <= 0) {
            logger.warn("No products found in cart for userId={}", userId);
            throw new OrderNotFoundException("No products found in cart for userId: " + userId);
        }
        logger.info("Total price for userId={} is {}", userId, total);
        return total;
    }

    /**
     * Retrieves the shopping cart ID for a given user.
     *
     * @param userId the ID of the user
     * @return shopping cart ID
     * @throws OrderNotFoundException if no cart exists
     */
    @Override
    public int getShoppingCartId(int userId) {
        logger.debug("Fetching shopping cart ID for userId={}", userId);
        int cartId = orderRepository.getShoppingCartId(userId);
        if (cartId == -1) {
            logger.warn("Shopping cart not found for userId={}", userId);
            throw new OrderNotFoundException("Shopping cart not found for userId: " + userId);
        }
        logger.info("Found shoppingCartId={} for userId={}", cartId, userId);
        return cartId;
    }
}
