package com.cts.ecommerce.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing the Products table in the ecommerce schema.
 * Maps to:
 *   Products(ProductId INT PK, ProductName VARCHAR(50) NOT NULL,
 *            Description VARCHAR(200), Price DOUBLE NOT NULL,
 *            CategoryId INT FK -> Category(CategoryId),
 *            ImageURL VARCHAR(200))
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer productId;

    @NotBlank(message = "Product name is required")
    @Size(max = 50, message = "Product name must not exceed 50 characters")
    private String productName;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    private Integer categoryId;

    @Size(max = 200, message = "Image URL must not exceed 200 characters")
    private String imageUrl;

    /**
     * Optional - populated via JOIN when fetching products with category.
     * Not stored in the Products table directly.
     */
    private String categoryName;
}