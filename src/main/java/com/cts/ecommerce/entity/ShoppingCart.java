package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ShoppingCart {

    private int shoppingCartId;
    private int userId;
    private boolean isActive;

}

