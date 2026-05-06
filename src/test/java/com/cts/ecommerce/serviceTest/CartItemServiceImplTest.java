package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.CartItem;
import com.cts.ecommerce.exception.CartItemNotFoundException;
import com.cts.ecommerce.exception.CartOperationException;
import com.cts.ecommerce.exception.ShoppingCartNotFoundException;
import com.cts.ecommerce.repository.CartItemRepository;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.impl.CartItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.cts.ecommerce.entity.ShoppingCart;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CartItemServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    private ShoppingCart shoppingCart;
    private CartItem cartItem;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
        shoppingCart.setShoppingCartId(1);
        shoppingCart.setUserId(10);
        shoppingCart.setActive(true);

        cartItem = new CartItem();
        cartItem.setCartItemId(1);
        cartItem.setShoppingCartId(1);
        cartItem.setProductId(100);
        cartItem.setQuantity(2);
    }

    /**
     * Tests adding a new item when the item does not already exist in cart.
     */
    @Test
    void addItem_ShouldAddNewItem_WhenItemDoesNotExistInCart() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(null);

        assertDoesNotThrow(() -> cartItemService.addItem(10, 100, 3));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, times(1)).addItem(1, 100, 3);
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests updating item quantity when the item already exists in cart.
     */
    @Test
    void addItem_ShouldUpdateQuantity_WhenItemAlreadyExistsInCart() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(cartItem);

        assertDoesNotThrow(() -> cartItemService.addItem(10, 100, 3));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, times(1)).updateQuantity(1, 100, 5);
        verify(cartItemRepository, never()).addItem(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests ShoppingCartNotFoundException when active cart is not found while adding item.
     */
    @Test
    void addItem_ShouldThrowShoppingCartNotFoundException_WhenActiveCartDoesNotExist() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(null);

        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class,
                () -> cartItemService.addItem(10, 100, 3)
        );

        assertTrue(exception.getMessage().contains("Active cart does not exist"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, never()).getItem(anyInt(), anyInt());
        verify(cartItemRepository, never()).addItem(anyInt(), anyInt(), anyInt());
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests CartOperationException when repository fails while adding item.
     */
    @Test
    void addItem_ShouldThrowCartOperationException_WhenRepositoryFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenThrow(new RuntimeException("Database error"));

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> cartItemService.addItem(10, 100, 3)
        );

        assertTrue(exception.getMessage().contains("Error occurred while adding item to cart"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, never()).addItem(anyInt(), anyInt(), anyInt());
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests removing item when remaining quantity is zero.
     */
    @Test
    void removeItem_ShouldRemoveItem_WhenRemainingQuantityIsZero() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(cartItem);

        assertDoesNotThrow(() -> cartItemService.removeItem(10, 100, 2));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, times(1)).removeItem(1, 100);
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests removing item when remaining quantity is less than zero.
     */
    @Test
    void removeItem_ShouldRemoveItem_WhenRemainingQuantityIsLessThanZero() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(cartItem);

        assertDoesNotThrow(() -> cartItemService.removeItem(10, 100, 5));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, times(1)).removeItem(1, 100);
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests updating quantity when remaining quantity is greater than zero.
     */
    @Test
    void removeItem_ShouldUpdateQuantity_WhenRemainingQuantityIsGreaterThanZero() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(cartItem);

        assertDoesNotThrow(() -> cartItemService.removeItem(10, 100, 1));

        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, times(1)).updateQuantity(1, 100, 1);
        verify(cartItemRepository, never()).removeItem(anyInt(), anyInt());
    }

    /**
     * Tests ShoppingCartNotFoundException when active cart is not found while removing item.
     */
    @Test
    void removeItem_ShouldThrowShoppingCartNotFoundException_WhenActiveCartDoesNotExist() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(null);

        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class,
                () -> cartItemService.removeItem(10, 100, 1)
        );

        assertTrue(exception.getMessage().contains("Active cart not found"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, never()).getItem(anyInt(), anyInt());
        verify(cartItemRepository, never()).removeItem(anyInt(), anyInt());
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests CartItemNotFoundException when item does not exist in cart.
     */
    @Test
    void removeItem_ShouldThrowCartItemNotFoundException_WhenItemDoesNotExistInCart() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(null);

        CartItemNotFoundException exception = assertThrows(
                CartItemNotFoundException.class,
                () -> cartItemService.removeItem(10, 100, 1)
        );

        assertTrue(exception.getMessage().contains("Item not found in cart"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, never()).removeItem(anyInt(), anyInt());
        verify(cartItemRepository, never()).updateQuantity(anyInt(), anyInt(), anyInt());
    }

    /**
     * Tests CartOperationException when repository fails while removing item.
     */
    @Test
    void removeItem_ShouldThrowCartOperationException_WhenRepositoryFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItem(1, 100)).thenReturn(cartItem);
        doThrow(new RuntimeException("Database error"))
                .when(cartItemRepository).removeItem(1, 100);

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> cartItemService.removeItem(10, 100, 2)
        );

        assertTrue(exception.getMessage().contains("Error occurred while removing item from cart"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItem(1, 100);
        verify(cartItemRepository, times(1)).removeItem(1, 100);
    }

    /**
     * Tests viewing active cart when active cart exists.
     */
    @Test
    void viewActiveCart_ShouldReturnCartItems_WhenActiveCartExists() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItemsByCartId(1)).thenReturn(List.of(cartItem));

        List<CartItem> result = cartItemService.viewActiveCart(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.getFirst().getProductId());
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItemsByCartId(1);
    }

    /**
     * Tests empty list when no active cart exists.
     */
    @Test
    void viewActiveCart_ShouldReturnEmptyList_WhenActiveCartDoesNotExist() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(null);

        List<CartItem> result = cartItemService.viewActiveCart(10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, never()).getItemsByCartId(anyInt());
    }

    /**
     * Tests CartOperationException when repository fails while viewing active cart.
     */
    @Test
    void viewActiveCart_ShouldThrowCartOperationException_WhenRepositoryFails() {
        when(shoppingCartRepository.findActiveCartByUserId(10)).thenReturn(shoppingCart);
        when(cartItemRepository.getItemsByCartId(1)).thenThrow(new RuntimeException("Database error"));

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> cartItemService.viewActiveCart(10)
        );

        assertTrue(exception.getMessage().contains("Error occurred while viewing active cart"));
        verify(shoppingCartRepository, times(1)).findActiveCartByUserId(10);
        verify(cartItemRepository, times(1)).getItemsByCartId(1);
    }

    /**
     * Tests fetching cart items by shopping cart ID successfully.
     */
    @Test
    void getItemsByShoppingCartId_ShouldReturnCartItems_WhenItemsExist() {
        when(cartItemRepository.getItemsByCartId(1)).thenReturn(List.of(cartItem));

        List<CartItem> result = cartItemService.getItemsByShoppingCartId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getShoppingCartId());
        verify(cartItemRepository, times(1)).getItemsByCartId(1);
    }

    /**
     * Tests CartOperationException when repository fails while fetching cart items.
     */
    @Test
    void getItemsByShoppingCartId_ShouldThrowCartOperationException_WhenRepositoryFails() {
        when(cartItemRepository.getItemsByCartId(1)).thenThrow(new RuntimeException("Database error"));

        CartOperationException exception = assertThrows(
                CartOperationException.class,
                () -> cartItemService.getItemsByShoppingCartId(1)
        );

        assertTrue(exception.getMessage().contains("Error fetching cart items"));
        verify(cartItemRepository, times(1)).getItemsByCartId(1);
    }
}

