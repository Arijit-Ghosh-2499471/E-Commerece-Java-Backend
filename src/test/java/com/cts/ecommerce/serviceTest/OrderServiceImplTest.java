package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.OrderRepository;
import com.cts.ecommerce.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(1);
        order.setUserId(10);
        order.setTotalPrice(2500.0);
        order.setShippingAddressId(5);
        order.setOrderStatus("Pending");
        order.setPaymentStatus("Paid");
        order.setShoppingCartId(20);
    }

    /**
     * Tests successful order creation.
     */
    @Test
    void addOrder_ShouldSaveOrder_WhenOrderIsCreatedSuccessfully() {
        when(orderRepository.addOrder(order)).thenReturn(1);

        assertDoesNotThrow(() -> orderService.addOrder(order));

        verify(orderRepository, times(1)).addOrder(order);
    }

    /**
     * Tests OrderCreationException when order creation returns zero rows.
     */
    @Test
    void addOrder_ShouldThrowOrderCreationException_WhenAddOrderReturnsZero() {
        when(orderRepository.addOrder(order)).thenReturn(0);

        OrderCreationException exception = assertThrows(
                OrderCreationException.class,
                () -> orderService.addOrder(order)
        );

        assertTrue(exception.getMessage().contains("Failed to create order"));
        verify(orderRepository, times(1)).addOrder(order);
    }

    /**
     * Tests fetching order by ID successfully.
     */
    @Test
    void findById_ShouldReturnOrder_WhenOrderExists() {
        when(orderRepository.findById(1)).thenReturn(order);

        Order result = orderService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
        assertEquals(10, result.getUserId());
        assertEquals(2500.0, result.getTotalPrice());
        verify(orderRepository, times(1)).findById(1);
    }

    /**
     * Tests OrderNotFoundException when repository throws EmptyResultDataAccessException.
     */
    @Test
    void findById_ShouldThrowOrderNotFoundException_WhenOrderDoesNotExist() {
        when(orderRepository.findById(1))
                .thenThrow(new EmptyResultDataAccessException(1));

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findById(1)
        );

        assertTrue(exception.getMessage().contains("Order not found with id"));
        verify(orderRepository, times(1)).findById(1);
    }

    /**
     * Tests fetching orders by user ID successfully.
     */
    @Test
    void findOrdersByUserId_ShouldReturnOrderList_WhenOrdersExist() {
        when(orderRepository.findOrdersByUserId(10)).thenReturn(List.of(order));

        List<Order> orders = orderService.findOrdersByUserId(10);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(10, orders.getFirst().getUserId());
        verify(orderRepository, times(1)).findOrdersByUserId(10);
    }

    /**
     * Tests OrderNotFoundException when orders by user ID returns empty list.
     */
    @Test
    void findOrdersByUserId_ShouldThrowOrderNotFoundException_WhenOrderListIsEmpty() {
        when(orderRepository.findOrdersByUserId(10)).thenReturn(List.of());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findOrdersByUserId(10)
        );

        assertTrue(exception.getMessage().contains("No orders found for userId"));
        verify(orderRepository, times(1)).findOrdersByUserId(10);
    }

    /**
     * Tests OrderNotFoundException when orders by user ID returns null.
     */
    @Test
    void findOrdersByUserId_ShouldThrowOrderNotFoundException_WhenOrderListIsNull() {
        when(orderRepository.findOrdersByUserId(10)).thenReturn(null);

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findOrdersByUserId(10)
        );

        assertTrue(exception.getMessage().contains("No orders found for userId"));
        verify(orderRepository, times(1)).findOrdersByUserId(10);
    }

    /**
     * Tests fetching all orders successfully.
     */
    @Test
    void findAll_ShouldReturnOrderList_WhenOrdersExist() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> orders = orderService.findAll();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1, orders.getFirst().getOrderId());
        verify(orderRepository, times(1)).findAll();
    }

    /**
     * Tests OrderNotFoundException when all orders list is empty.
     */
    @Test
    void findAll_ShouldThrowOrderNotFoundException_WhenOrderListIsEmpty() {
        when(orderRepository.findAll()).thenReturn(List.of());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findAll()
        );

        assertTrue(exception.getMessage().contains("No orders found in the system"));
        verify(orderRepository, times(1)).findAll();
    }

    /**
     * Tests OrderNotFoundException when all orders list is null.
     */
    @Test
    void findAll_ShouldThrowOrderNotFoundException_WhenOrderListIsNull() {
        when(orderRepository.findAll()).thenReturn(null);

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findAll()
        );

        assertTrue(exception.getMessage().contains("No orders found in the system"));
        verify(orderRepository, times(1)).findAll();
    }

    /**
     * Tests successful order status update.
     */
    @Test
    void updateOrderStatus_ShouldReturnOne_WhenStatusIsUpdatedSuccessfully() {
        when(orderRepository.updateOrderStatus(1, "Delivered")).thenReturn(1);

        int result = orderService.updateOrderStatus(1, "Delivered");

        assertEquals(1, result);
        verify(orderRepository, times(1)).updateOrderStatus(1, "Delivered");
    }

    /**
     * Tests OrderUpdateException when order status update returns zero rows.
     */
    @Test
    void updateOrderStatus_ShouldThrowOrderUpdateException_WhenUpdateReturnsZero() {
        when(orderRepository.updateOrderStatus(1, "Delivered")).thenReturn(0);

        OrderUpdateException exception = assertThrows(
                OrderUpdateException.class,
                () -> orderService.updateOrderStatus(1, "Delivered")
        );

        assertTrue(exception.getMessage().contains("Failed to update status"));
        verify(orderRepository, times(1)).updateOrderStatus(1, "Delivered");
    }

    /**
     * Tests successful payment processing.
     */
    @Test
    void processPayment_ShouldReturnOne_WhenPaymentIsProcessedSuccessfully() {
        when(orderRepository.processPayment(1, "Paid")).thenReturn(1);

        int result = orderService.processPayment(1, "Paid");

        assertEquals(1, result);
        verify(orderRepository, times(1)).processPayment(1, "Paid");
    }

    /**
     * Tests PaymentProcessingException when payment processing returns zero rows.
     */
    @Test
    void processPayment_ShouldThrowPaymentProcessingException_WhenPaymentUpdateReturnsZero() {
        when(orderRepository.processPayment(1, "Paid")).thenReturn(0);

        PaymentProcessingException exception = assertThrows(
                PaymentProcessingException.class,
                () -> orderService.processPayment(1, "Paid")
        );

        assertTrue(exception.getMessage().contains("Failed to process payment"));
        verify(orderRepository, times(1)).processPayment(1, "Paid");
    }

    /**
     * Tests fetching cart products successfully.
     */
    @Test
    void getCartProducts_ShouldReturnProductList_WhenProductsExist() {
        Map<String, Object> product = Map.of(
                "productId", 100,
                "productName", "Laptop",
                "price", 2500.0
        );

        when(orderRepository.getCartProducts(10)).thenReturn(List.of(product));

        List<Map<String, Object>> products = orderService.getCartProducts(10);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Laptop", products.getFirst().get("productName"));
        verify(orderRepository, times(1)).getCartProducts(10);
    }

    /**
     * Tests OrderNotFoundException when cart products list is empty.
     */
    @Test
    void getCartProducts_ShouldThrowOrderNotFoundException_WhenProductListIsEmpty() {
        when(orderRepository.getCartProducts(10)).thenReturn(List.of());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getCartProducts(10)
        );

        assertTrue(exception.getMessage().contains("No cart products found for userId"));
        verify(orderRepository, times(1)).getCartProducts(10);
    }

    /**
     * Tests OrderNotFoundException when cart products list is null.
     */
    @Test
    void getCartProducts_ShouldThrowOrderNotFoundException_WhenProductListIsNull() {
        when(orderRepository.getCartProducts(10)).thenReturn(null);

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getCartProducts(10)
        );

        assertTrue(exception.getMessage().contains("No cart products found for userId"));
        verify(orderRepository, times(1)).getCartProducts(10);
    }

    /**
     * Tests calculating total price successfully.
     */
    @Test
    void calculateTotalPrice_ShouldReturnTotalPrice_WhenProductsExistInCart() {
        when(orderRepository.calculateTotalPrice(10)).thenReturn(2500.0);

        double result = orderService.calculateTotalPrice(10);

        assertEquals(2500.0, result);
        verify(orderRepository, times(1)).calculateTotalPrice(10);
    }

    /**
     * Tests OrderNotFoundException when total price is zero.
     */
    @Test
    void calculateTotalPrice_ShouldThrowOrderNotFoundException_WhenTotalPriceIsZero() {
        when(orderRepository.calculateTotalPrice(10)).thenReturn(0.0);

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.calculateTotalPrice(10)
        );

        assertTrue(exception.getMessage().contains("No products found in cart"));
        verify(orderRepository, times(1)).calculateTotalPrice(10);
    }

    /**
     * Tests OrderNotFoundException when total price is negative.
     */
    @Test
    void calculateTotalPrice_ShouldThrowOrderNotFoundException_WhenTotalPriceIsNegative() {
        when(orderRepository.calculateTotalPrice(10)).thenReturn(-100.0);

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.calculateTotalPrice(10)
        );

        assertTrue(exception.getMessage().contains("No products found in cart"));
        verify(orderRepository, times(1)).calculateTotalPrice(10);
    }

    /**
     * Tests fetching shopping cart ID successfully.
     */
    @Test
    void getShoppingCartId_ShouldReturnCartId_WhenCartExists() {
        when(orderRepository.getShoppingCartId(10)).thenReturn(20);

        int result = orderService.getShoppingCartId(10);

        assertEquals(20, result);
        verify(orderRepository, times(1)).getShoppingCartId(10);
    }

    /**
     * Tests OrderNotFoundException when shopping cart ID is not found.
     */
    @Test
    void getShoppingCartId_ShouldThrowOrderNotFoundException_WhenCartDoesNotExist() {
        when(orderRepository.getShoppingCartId(10)).thenReturn(-1);

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getShoppingCartId(10)
        );

        assertTrue(exception.getMessage().contains("Shopping cart not found for userId"));
        verify(orderRepository, times(1)).getShoppingCartId(10);
    }
}