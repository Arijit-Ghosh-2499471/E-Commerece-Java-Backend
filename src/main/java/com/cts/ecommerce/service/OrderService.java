package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    int addOrder(Order order);
    Order findById(int orderId);
    List<Order> findOrdersByUserId(int userId);
    List<Order> findAll();
    int updateOrderStatus(int orderId,String status);
    int processPayment(int orderId,String paymentStatus);
    List<Map<String, Object>> getCartProducts(int userId);
    double calculateTotalPrice(int userId);
    int getShoppingCartId(int userId);
}
