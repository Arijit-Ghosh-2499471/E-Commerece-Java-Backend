package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.exception.ResourceNotFoundException;
import com.cts.ecommerce.model.Product;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .productId(1)
                .productName("Laptop")
                .description("Gaming Laptop")
                .price(1500.0)
                .categoryId(2)
                .imageUrl("http://img/laptop.png")
                .categoryName("Electronics")
                .build();
    }

    @Test
    @DisplayName("createProduct should save when category exists")
    void createProduct_shouldSucceed_whenCategoryExists() {
        when(categoryRepository.existsById(2)).thenReturn(true);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(sampleProduct));

        Product result = productService.createProduct(sampleProduct);

        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Laptop");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("createProduct should save when categoryId is null")
    void createProduct_shouldSucceed_whenCategoryIdIsNull() {
        Product noCategory = Product.builder()
                .productName("Misc Item")
                .price(10.0)
                .build();
        when(productRepository.findById(any())).thenReturn(Optional.of(noCategory));

        Product result = productService.createProduct(noCategory);

        assertThat(result).isNotNull();
        verify(productRepository).save(noCategory);
        verify(categoryRepository, never()).existsById(any());
    }

    @Test
    @DisplayName("createProduct should throw when referenced category missing")
    void createProduct_shouldThrow_whenCategoryMissing() {
        when(categoryRepository.existsById(2)).thenReturn(false);

        assertThatThrownBy(() -> productService.createProduct(sampleProduct))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateProduct should update when product and category exist")
    void updateProduct_shouldSucceed() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(2)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        Product input = Product.builder()
                .productName("Updated Laptop")
                .price(1700.0)
                .categoryId(2)
                .build();

        Product result = productService.updateProduct(1, input);

        assertThat(result).isNotNull();
        verify(productRepository).update(any(Product.class));
    }

    @Test
    @DisplayName("updateProduct should throw when product not found")
    void updateProduct_shouldThrow_whenProductNotFound() {
        when(productRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> productService.updateProduct(99, sampleProduct))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(productRepository, never()).update(any());
    }

    @Test
    @DisplayName("updateProduct should throw when referenced category missing")
    void updateProduct_shouldThrow_whenCategoryMissing() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(2)).thenReturn(false);

        assertThatThrownBy(() -> productService.updateProduct(1, sampleProduct))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    @DisplayName("deleteProduct should remove when exists")
    void deleteProduct_shouldDelete_whenExists() {
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.deleteById(1)).thenReturn(1);

        productService.deleteProduct(1);

        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteProduct should throw when not found")
    void deleteProduct_shouldThrow_whenNotFound() {
        when(productRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteProduct(99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("getProductById should return product when found")
    void getProductById_shouldReturn_whenFound() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductById(1);

        assertThat(result.getProductId()).isEqualTo(1);
        assertThat(result.getProductName()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("getProductById should throw when not found")
    void getProductById_shouldThrow_whenNotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    @DisplayName("getAllProducts should return all products")
    void getAllProducts_shouldReturnList() {
        Product another = Product.builder().productId(2).productName("Phone").price(800.0).build();
        when(productRepository.findAll()).thenReturn(Arrays.asList(sampleProduct, another));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("getProductsByCategory should return list when category exists")
    void getProductsByCategory_shouldReturnList() {
        when(categoryRepository.existsById(2)).thenReturn(true);
        when(productRepository.findByCategoryId(2)).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.getProductsByCategory(2);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategoryId()).isEqualTo(2);
    }

    @Test
    @DisplayName("getProductsByCategory should throw when category not found")
    void getProductsByCategory_shouldThrow_whenCategoryNotFound() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> productService.getProductsByCategory(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(productRepository, never()).findByCategoryId(any());
    }
}