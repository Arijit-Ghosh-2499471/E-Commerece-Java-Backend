package com.cts.ecommerce.serviceTest.impl;

import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.exception.OrderNotFoundException;
import com.cts.ecommerce.exception.OrderCreationException;
import com.cts.ecommerce.exception.OrderUpdateException;
import com.cts.ecommerce.exception.PaymentProcessingException;
import com.cts.ecommerce.repository.OrderRepository;
import com.cts.ecommerce.serviceTest.OrderService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(Order order) {
        int rows = orderRepository.addOrder(order);
        if (rows == 0) {
            throw new OrderCreationException("Failed to create order for userId: " + order.getUserId());
        }
    }

    @Override
    public Order findById(int orderId) {
        try {
            return orderRepository.findById(orderId);
        } catch (EmptyResultDataAccessException ex) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for userId: " + userId);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found in the system");
        }
        return orders;
    }

    @Override
    public int updateOrderStatus(int orderId, String status) {
        int rows = orderRepository.updateOrderStatus(orderId, status);
        if (rows == 0) {
            throw new OrderUpdateException("Failed to update status for orderId: " + orderId);
        }
        return rows;
    }

    @Override
    public int processPayment(int orderId, String paymentStatus) {
        int rows = orderRepository.processPayment(orderId, paymentStatus);
        if (rows == 0) {
            throw new PaymentProcessingException("Failed to process payment for orderId: " + orderId);
        }
        return rows;
    }

    @Override
    public List<Map<String, Object>> getCartProducts(int userId) {
        List<Map<String, Object>> products = orderRepository.getCartProducts(userId);
        if (products == null || products.isEmpty()) {
            throw new OrderNotFoundException("No cart products found for userId: " + userId);
        }
        return products;
    }

    @Override
    public double calculateTotalPrice(int userId) {
        double total = orderRepository.calculateTotalPrice(userId);
        if (total <= 0) {
            throw new OrderNotFoundException("No products found in cart for userId: " + userId);
        }
        return total;
    }

    @Override
    public int getShoppingCartId(int userId) {
        int cartId = orderRepository.getShoppingCartId(userId);
        if (cartId == -1) {
            throw new OrderNotFoundException("Shopping cart not found for userId: " + userId);
        }
        return cartId;
    }
}
