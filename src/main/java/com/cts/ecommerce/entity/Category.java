package com.cts.ecommerce.entity;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Entity representing the Category table in the ecommerce schema.
 * Maps to:
 *   Category(CategoryId INT PK, CategoryName VARCHAR(20) NOT NULL)
 */

@Getter
@Setter
@NoArgsConstructor
@Component
public class Category {

    private Integer categoryId;
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
