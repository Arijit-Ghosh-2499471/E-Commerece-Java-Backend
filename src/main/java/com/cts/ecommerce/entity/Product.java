package com.cts.ecommerce.entity;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Entity representing the Products table in the ecommerce schema.
 * Maps to:
 *   Products(ProductId INT PK, ProductName VARCHAR, Description VARCHAR,
 *      Price DOUBLE, CategoryId INT FK, ImageUrl VARCHAR)
 */

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