package com.cts.ecommerce.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Product {

    private Integer productId;
    private String productName;
    private String description;
    private Double price;
    private Integer categoryId;
    private String imageUrl;

    public Product(String productName, String description, Double price, Integer categoryId, String imageUrl) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }
}