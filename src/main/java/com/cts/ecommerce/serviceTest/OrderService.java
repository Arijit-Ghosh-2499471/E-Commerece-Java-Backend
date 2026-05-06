package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing {@link Order} entities.
 * Provides CRUD operations, payment processing, and shopping cart queries.
 */
public interface OrderService {

    /**
     * Adds a new order to the system.
     *
     * @param order the {@link Order} entity to be saved
     */
    void addOrder(Order order);

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order
     * @return the {@link Order} entity if found
     */
    Order findById(int orderId);

    /**
     * Retrieves all orders associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Order} entities for the user
     */
    List<Order> findOrdersByUserId(int userId);

    /**
     * Retrieves all orders in the system.
     *
     * @return list of all {@link Order} entities
     */
    List<Order> findAll();

    /**
     * Updates the status of an order.
     *
     * @param orderId the ID of the order
     * @param status  the new status value
     * @return number of rows affected (0 if update failed)
     */
    int updateOrderStatus(int orderId, String status);

    /**
     * Processes payment for an order.
     *
     * @param orderId       the ID of the order
     * @param paymentStatus the new payment status
     * @return number of rows affected (0 if update failed)
     */
    int processPayment(int orderId, String paymentStatus);

    /**
     * Retrieves products currently in the user's shopping cart.
     *
     * @param userId the ID of the user
     * @return list of cart products represented as maps of field values
     */
    List<Map<String, Object>> getCartProducts(int userId);

    /**
     * Calculates the total price of products in the user's shopping cart.
     *
     * @param userId the ID of the user
     * @return total price of cart items
     */
    double calculateTotalPrice(int userId);

    /**
     * Retrieves the shopping cart ID for a given user.
     *
     * @param userId the ID of the user
     * @return shopping cart ID, or -1 if none exists
     */
    int getShoppingCartId(int userId);
}
