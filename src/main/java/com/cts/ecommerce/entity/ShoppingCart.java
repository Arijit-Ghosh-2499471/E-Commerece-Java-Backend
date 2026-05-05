package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ShoppingCart {

    private int shoppingCartId;
    private int userId;
    private boolean isActive;

    public ShoppingCart(int userId, boolean isActive) {
        this.userId = userId;
        this.isActive = isActive;
    }

}

