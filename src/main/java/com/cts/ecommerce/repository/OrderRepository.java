package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderRepository {

    int addOrder(Order order);

    Order findById(int orderId);
    List<Order> findOrdersByUserId(int userId);
    List<Order> findAll();
    int updateOrderStatus(int orderId,String status);
    int processPayment(int orderId,String paymentStatus);
    List<Map<String, Object>> getCartProducts(int userId);
    double caluculateTotalPrice(int userId);
    int getShoppingCartId(int userId);
}
