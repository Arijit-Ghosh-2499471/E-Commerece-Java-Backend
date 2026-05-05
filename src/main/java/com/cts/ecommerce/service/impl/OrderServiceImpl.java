package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.repository.OrderRepository;
import com.cts.ecommerce.service.OrderService;
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
        orderRepository.addOrder(order);
    }

    @Override
    public Order findById(int orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public int updateOrderStatus(int orderId, String status) {
        return orderRepository.updateOrderStatus(orderId,status);
    }

    @Override
    public int processPayment(int orderId, String paymentStatus){
        return orderRepository.processPayment(orderId,paymentStatus);
    }

    @Override
    public List<Map<String, Object>> getCartProducts(int userId) {
        return orderRepository.getCartProducts(userId);
    }

    @Override
    public double calculateTotalPrice(int userId) {
        return orderRepository.calculateTotalPrice(userId);
    }

    @Override
    public int getShoppingCartId(int userId) {
        return orderRepository.getShoppingCartId(userId);
    }
}
