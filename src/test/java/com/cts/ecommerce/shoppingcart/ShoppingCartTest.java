package com.cts.ecommerce.shoppingcart;

import com.cts.ecommerce.entity.ShoppingCart;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    void testCreateAndFetchActiveCart() {

        // User already exists in DB (Customer)
        int userId = 1; // assuming Aayush Chowdhury has UserId = 1

        ShoppingCart cart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            shoppingCartRepository.createCart(userId);
            cart = shoppingCartRepository.findActiveCartByUserId(userId);
        }

        assertNotNull(cart);
        assertEquals(userId, cart.getUserId());
        assertTrue(cart.isActive());
    }

    @Test
    void testDeactivateCart() {

        int userId = 1;

        ShoppingCart cart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        if (cart == null) {
            shoppingCartRepository.createCart(userId);
            cart = shoppingCartRepository.findActiveCartByUserId(userId);
        }

        shoppingCartRepository.deactivateCart(cart.getShoppingCartId());

        ShoppingCart inactiveCart =
                shoppingCartRepository.findActiveCartByUserId(userId);

        assertNull(inactiveCart);
    }
}