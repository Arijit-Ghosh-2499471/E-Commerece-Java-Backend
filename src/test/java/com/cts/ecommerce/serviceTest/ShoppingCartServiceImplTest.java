package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.exception.CartOperationException;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ShoppingCartServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private ShoppingCart shoppingCart;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(1);
        shoppingCart.setUserId(10);
        shoppingCart.setActive(true);
    }

    /**
     * Tests successful checkout when an active cart exists.
     */
    @Test
    void checkout_ShouldDeactivateCartAndCreateNewCart_WhenActiveCartExists() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);

        assertDoesNotThrow(() -> shoppingCartService.checkout(10));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(shoppingCartRepository, times(1)).deactivateCart(1);
        verify(shoppingCartRepository, times(1)).createCart(10);
    }

    /**
     * Tests checkout when no active cart exists.
     * The service catches ShoppingCartNotFoundException and skips checkout.
     */
    @Test
    void checkout_ShouldSkipCheckout_WhenActiveCartDoesNotExist() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(null);

        assertDoesNotThrow(() -> shoppingCartService.checkout(10));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(shoppingCartRepository, never()).deactivateCart(anyInt());
        verify(shoppingCartRepository, never()).createCart(anyInt());
    }

    /**
     * Tests CartOperationException when finding active cart fails during checkout.
     */
    @Test
    void checkout_ShouldThrowCartOperationException_WhenFindActiveCartFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10))
                .thenThrow(new RuntimeException("Database error"));

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> shoppingCartService.checkout(10)
        );

        assertTrue(exception.getMessage().contains("Checkout failed"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(shoppingCartRepository, never()).deactivateCart(anyInt());
        verify(shoppingCartRepository, never()).createCart(anyInt());
    }

    /**
     * Tests CartOperationException when cart deactivation fails during checkout.
     */
    @Test
    void checkout_ShouldThrowCartOperationException_WhenDeactivateCartFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        doThrow(new RuntimeException("Database error"))
                .when(shoppingCartRepository).deactivateCart(1);

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> shoppingCartService.checkout(10)
        );

        assertTrue(exception.getMessage().contains("Checkout failed"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(shoppingCartRepository, times(1)).deactivateCart(1);
        verify(shoppingCartRepository, never()).createCart(anyInt());
    }

    /**
     * Tests CartOperationException when creating new cart fails during checkout.
     */
    @Test
    void checkout_ShouldThrowCartOperationException_WhenCreateCartFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        doNothing().when(shoppingCartRepository).deactivateCart(1);
        doThrow(new RuntimeException("Database error"))
                .when(shoppingCartRepository).createCart(10);

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> shoppingCartService.checkout(10)
        );

        assertTrue(exception.getMessage().contains("Checkout failed"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(shoppingCartRepository, times(1)).deactivateCart(1);
        verify(shoppingCartRepository, times(1)).createCart(10);
    }

    /**
     * Tests fetching active shopping cart successfully.
     */
    @Test
    void findActiveCardByUserId_ShouldReturnShoppingCart_WhenActiveCartExists() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);

        ShoppingCart result = shoppingCartService.findActiveCardByUserId(10);

        assertNotNull(result);
        assertEquals(1, result.getShoppingCartId());
        assertEquals(10, result.getUserId());
        assertTrue(result.isActive());
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
    }

    /**
     * Tests null when no active shopping cart exists.
     */
    @Test
    void findActiveCardByUserId_ShouldReturnNull_WhenActiveCartDoesNotExist() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(null);

        ShoppingCart result = shoppingCartService.findActiveCardByUserId(10);

        assertNull(result);
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
    }

    /**
     * Tests CartOperationException when repository fails while fetching active cart.
     */
    @Test
    void findActiveCardByUserId_ShouldThrowCartOperationException_WhenRepositoryFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10))
                .thenThrow(new RuntimeException("Database error"));

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> shoppingCartService.findActiveCardByUserId(10)
        );

        assertTrue(exception.getMessage().contains("Error fetching active cart"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
    }
}
