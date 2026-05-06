package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Product;
import com.cts.ecommerce.exception.CategoryNotFoundException;
import com.cts.ecommerce.exception.InvalidInputException;
import com.cts.ecommerce.exception.ProductCreationException;
import com.cts.ecommerce.exception.ProductDeletionException;
import com.cts.ecommerce.exception.ProductNotFoundException;
import com.cts.ecommerce.exception.ProductRetrievalException;
import com.cts.ecommerce.exception.ProductUpdateException;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.repository.ProductRepository;
import com.cts.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProductServiceImpl}.
 * <p>
 * Uses Mockito to stub both {@link ProductRepository} and
 * {@link CategoryRepository} so the service can be exercised without
 * touching the database.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1);
        product.setProductName("Wireless Headphones");
        product.setDescription("Over-ear bluetooth headphones");
        product.setPrice(2499.00);
        product.setCategoryId(1);
        product.setImageUrl("https://cdn.shop.com/img/headphones.jpg");
    }

    // ---------------------------------------------------------------
    //  createProduct
    // ---------------------------------------------------------------

    /**
     * Tests successful product creation when the category exists.
     */
    @Test
    void createProduct_ShouldReturnProduct_WhenCategoryExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.save(product)).thenReturn(1);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Wireless Headphones", result.getProductName());
        verify(categoryRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).save(product);
    }

    /**
     * Tests CategoryNotFoundException when the supplied category id does not exist.
     */
    @Test
    void createProduct_ShouldThrowCategoryNotFoundException_WhenCategoryMissing() {
        when(categoryRepository.existsById(1)).thenReturn(false);

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> productService.createProduct(product)
        );

        assertTrue(ex.getMessage().contains("Category not found"));
        verify(categoryRepository, times(1)).existsById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    /**
     * Tests that a null categoryId is silently accepted (matches original
     * service contract: a product without a category is allowed).
     */
    @Test
    void createProduct_ShouldAllowNullCategoryId() {
        product.setCategoryId(null);
        when(productRepository.save(product)).thenReturn(1);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        verify(categoryRepository, never()).existsById(anyInt());
        verify(productRepository, times(1)).save(product);
    }

    /**
     * Tests that a ProductCreationException raised by the repository is propagated.
     */
    @Test
    void createProduct_ShouldPropagateCreationException_WhenRepositoryThrows() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.save(product))
                .thenThrow(new ProductCreationException("DB constraint violation"));

        ProductCreationException ex = assertThrows(
                ProductCreationException.class,
                () -> productService.createProduct(product)
        );

        assertTrue(ex.getMessage().contains("DB constraint violation"));
        verify(productRepository, times(1)).save(product);
    }

    // ---------------------------------------------------------------
    //  updateProduct
    // ---------------------------------------------------------------

    /**
     * Tests successful product update when both product and category exist.
     */
    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductAndCategoryExist() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.update(any(Product.class))).thenReturn(1);

        Product result = productService.updateProduct(1, product);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
        verify(productRepository, times(1)).existsById(1);
        verify(categoryRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).update(product);
    }

    /**
     * Tests ProductNotFoundException when updating a product that does not exist.
     */
    @Test
    void updateProduct_ShouldThrowNotFoundException_WhenProductMissing() {
        when(productRepository.existsById(99)).thenReturn(false);

        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(99, product)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(productRepository, times(1)).existsById(99);
        verify(productRepository, never()).update(any(Product.class));
    }

    /**
     * Tests CategoryNotFoundException when updating with an invalid category id.
     */
    @Test
    void updateProduct_ShouldThrowCategoryNotFoundException_WhenCategoryMissing() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(false);

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> productService.updateProduct(1, product)
        );

        assertTrue(ex.getMessage().contains("Category not found"));
        verify(productRepository, never()).update(any(Product.class));
    }

    /**
     * Tests that a ProductUpdateException raised by the repository is propagated.
     */
    @Test
    void updateProduct_ShouldPropagateUpdateException_WhenRepositoryThrows() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.update(any(Product.class)))
                .thenThrow(new ProductUpdateException("DB error"));

        ProductUpdateException ex = assertThrows(
                ProductUpdateException.class,
                () -> productService.updateProduct(1, product)
        );

        assertTrue(ex.getMessage().contains("DB error"));
        verify(productRepository, times(1)).update(any(Product.class));
    }

    // ---------------------------------------------------------------
    //  deleteProduct
    // ---------------------------------------------------------------

    /**
     * Tests successful product deletion when the target exists.
     */
    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.deleteById(1)).thenReturn(1);

        assertDoesNotThrow(() -> productService.deleteProduct(1));

        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    /**
     * Tests ProductNotFoundException when deleting a product that does not exist.
     */
    @Test
    void deleteProduct_ShouldThrowNotFoundException_WhenProductMissing() {
        when(productRepository.existsById(99)).thenReturn(false);

        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(99)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(productRepository, times(1)).existsById(99);
        verify(productRepository, never()).deleteById(anyInt());
    }

    /**
     * Tests that a ProductDeletionException raised by the repository is propagated.
     */
    @Test
    void deleteProduct_ShouldPropagateDeletionException_WhenRepositoryThrows() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.deleteById(1))
                .thenThrow(new ProductDeletionException("FK constraint"));

        ProductDeletionException ex = assertThrows(
                ProductDeletionException.class,
                () -> productService.deleteProduct(1)
        );

        assertTrue(ex.getMessage().contains("FK constraint"));
        verify(productRepository, times(1)).deleteById(1);
    }

    // ---------------------------------------------------------------
    //  getProductById
    // ---------------------------------------------------------------

    /**
     * Tests fetching an existing product by id.
     */
    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        when(productRepository.findById(1)).thenReturn(product);

        Product result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
        verify(productRepository, times(1)).findById(1);
    }

    /**
     * Tests ProductNotFoundException when fetching a product that does not exist.
     */
    @Test
    void getProductById_ShouldThrowNotFoundException_WhenProductMissing() {
        when(productRepository.findById(99))
                .thenThrow(new ProductNotFoundException("Product not found with id 99"));

        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(99)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(productRepository, times(1)).findById(99);
    }

    // ---------------------------------------------------------------
    //  getAllProducts
    // ---------------------------------------------------------------

    /**
     * Tests fetching all products when results are present.
     */
    @Test
    void getAllProducts_ShouldReturnList_WhenProductsExist() {
        Product second = new Product();
        second.setProductId(2);
        second.setProductName("Smartphone");
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, second));

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Tests fetching all products when none exist (empty list).
     */
    @Test
    void getAllProducts_ShouldReturnEmptyList_WhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Tests that a ProductRetrievalException raised by the repository is propagated.
     */
    @Test
    void getAllProducts_ShouldPropagateRetrievalException_WhenRepositoryThrows() {
        when(productRepository.findAll())
                .thenThrow(new ProductRetrievalException("DB error"));

        ProductRetrievalException ex = assertThrows(
                ProductRetrievalException.class,
                () -> productService.getAllProducts()
        );

        assertTrue(ex.getMessage().contains("DB error"));
        verify(productRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------------
    //  getProductsByCategory
    // ---------------------------------------------------------------

    /**
     * Tests fetching products by category when the category exists.
     */
    @Test
    void getProductsByCategory_ShouldReturnList_WhenCategoryExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(productRepository.findByCategoryId(1))
                .thenReturn(Collections.singletonList(product));

        List<Product> result = productService.getProductsByCategory(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).findByCategoryId(1);
    }

    /**
     * Tests CategoryNotFoundException when the requested category does not exist.
     */
    @Test
    void getProductsByCategory_ShouldThrowCategoryNotFoundException_WhenCategoryMissing() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> productService.getProductsByCategory(99)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(productRepository, never()).findByCategoryId(anyInt());
    }

    // ---------------------------------------------------------------
    //  getProductsByName
    // ---------------------------------------------------------------

    /**
     * Tests successful name search with a non-empty query string.
     */
    @Test
    void getProductsByName_ShouldReturnList_WhenNameIsValid() {
        when(productRepository.findByProductNameContaining("Headphones"))
                .thenReturn(Collections.singletonList(product));

        List<Product> result = productService.getProductsByName("Headphones");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1))
                .findByProductNameContaining("Headphones");
    }

    /**
     * Tests that a leading/trailing whitespace-only query is treated as blank
     * AFTER trimming, but a query with leading/trailing whitespace is trimmed.
     * Here we verify the trim happens before the repo call.
     */
    @Test
    void getProductsByName_ShouldTrimInput_BeforeQuerying() {
        when(productRepository.findByProductNameContaining("Phone"))
                .thenReturn(Collections.emptyList());

        productService.getProductsByName("  Phone  ");

        verify(productRepository, times(1)).findByProductNameContaining("Phone");
    }

    /**
     * Tests InvalidInputException when the name parameter is null.
     */
    @Test
    void getProductsByName_ShouldThrowInvalidInputException_WhenNameIsNull() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByName(null)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
        verify(productRepository, never()).findByProductNameContaining(anyString());
    }

    /**
     * Tests InvalidInputException when the name parameter is an empty string.
     */
    @Test
    void getProductsByName_ShouldThrowInvalidInputException_WhenNameIsEmpty() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByName("")
        );

        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
        verify(productRepository, never()).findByProductNameContaining(anyString());
    }

    /**
     * Tests InvalidInputException when the name parameter is whitespace only.
     */
    @Test
    void getProductsByName_ShouldThrowInvalidInputException_WhenNameIsBlank() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByName("   ")
        );

        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
        verify(productRepository, never()).findByProductNameContaining(anyString());
    }

    // ---------------------------------------------------------------
    //  getProductsByPriceRange
    // ---------------------------------------------------------------

    /**
     * Tests successful price-range query when bounds are valid.
     */
    @Test
    void getProductsByPriceRange_ShouldReturnList_WhenRangeIsValid() {
        when(productRepository.findByPriceBetween(100.0, 5000.0))
                .thenReturn(Collections.singletonList(product));

        List<Product> result = productService.getProductsByPriceRange(100.0, 5000.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByPriceBetween(100.0, 5000.0);
    }

    /**
     * Tests InvalidInputException when minPrice is null.
     */
    @Test
    void getProductsByPriceRange_ShouldThrowInvalidInputException_WhenMinPriceNull() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByPriceRange(null, 5000.0)
        );

        assertTrue(ex.getMessage().contains("required"));
        verify(productRepository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    /**
     * Tests InvalidInputException when maxPrice is null.
     */
    @Test
    void getProductsByPriceRange_ShouldThrowInvalidInputException_WhenMaxPriceNull() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByPriceRange(100.0, null)
        );

        assertTrue(ex.getMessage().contains("required"));
        verify(productRepository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    /**
     * Tests InvalidInputException when a negative price is supplied.
     */
    @Test
    void getProductsByPriceRange_ShouldThrowInvalidInputException_WhenPriceNegative() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByPriceRange(-1.0, 5000.0)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("negative"));
        verify(productRepository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    /**
     * Tests InvalidInputException when minPrice is greater than maxPrice.
     */
    @Test
    void getProductsByPriceRange_ShouldThrowInvalidInputException_WhenMinExceedsMax() {
        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> productService.getProductsByPriceRange(5000.0, 100.0)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("greater"));
        verify(productRepository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    // ---------------------------------------------------------------
    //  validateProductId
    // ---------------------------------------------------------------

    /**
     * Tests that validateProductId completes silently when the product exists.
     */
    @Test
    void validateProductId_ShouldNotThrow_WhenProductExists() {
        when(productRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> productService.validateProductId(1));

        verify(productRepository, times(1)).existsById(1);
    }

    /**
     * Tests ProductNotFoundException when validating an id that does not exist.
     */
    @Test
    void validateProductId_ShouldThrowNotFoundException_WhenProductMissing() {
        when(productRepository.existsById(99)).thenReturn(false);

        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productService.validateProductId(99)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(productRepository, times(1)).existsById(99);
    }
}