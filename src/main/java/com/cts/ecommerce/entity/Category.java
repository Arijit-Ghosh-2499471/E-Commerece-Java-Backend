package com.cts.ecommerce.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing the Category table in the ecommerce schema.
 * Maps to:
 *   Category(CategoryId INT PK, CategoryName VARCHAR(20) NOT NULL)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Integer categoryId;

    @NotBlank(message = "Category name is required")
    @Size(max = 20, message = "Category name must not exceed 20 characters")
    private String categoryName;
}
