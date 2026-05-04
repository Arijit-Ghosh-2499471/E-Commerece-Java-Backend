package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CartItem {

    private int cartItemId;
    private int shoppingCartId;
    private int productId;
    private int quantity;

}
