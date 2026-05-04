package com.cts.ecommerce.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Entity representing the Products table in the ecommerce schema.
 * Maps to:
 *   Products(ProductId INT PK, ProductName VARCHAR(50) NOT NULL,
 *            Description VARCHAR(200), Price DOUBLE NOT NULL,
 *            CategoryId INT FK -> Category(CategoryId),
 *            ImageURL VARCHAR(200))
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

//    /**
//     * Optional - populated via JOIN when fetching products with category.
//     * Not stored in the Products table directly.
//     */
//    private String categoryName;
}