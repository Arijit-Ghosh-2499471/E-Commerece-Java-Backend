package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Entity representing the CartItems table in the ecommerce schema.
 * Maps to:
 *   CartItems(CartItemId INT PK, ShoppingCartId INT FK,
 *      ProductId INT FK, Quantity INT)
 */

@Getter
@Setter
@NoArgsConstructor
@Component
public class CartItem {

    private int cartItemId;
    private int shoppingCartId;
    private int productId;
    private int quantity;

    public CartItem(int shoppingCartId, int productId, int quantity) {
        this.shoppingCartId = shoppingCartId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
