package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductRepositoryImpl productDao;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .productName("Laptop")
                .description("Gaming Laptop")
                .price(1500.0)
                .categoryId(2)
                .imageUrl("http://img/laptop.png")
                .build();
    }

    @Test
    @DisplayName("save should insert and populate generated product id")
    void save_shouldInsertAndReturnGeneratedId() {
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 100));
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        int result = productDao.save(sampleProduct);

        assertThat(result).isEqualTo(100);
        assertThat(sampleProduct.getProductId()).isEqualTo(100);
    }

    @Test
    @DisplayName("save should handle null categoryId without error")
    void save_shouldHandleNullCategoryId() {
        Product noCategory = Product.builder()
                .productName("Misc")
                .price(5.0)
                .build();

        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 50));
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        int result = productDao.save(noCategory);

        assertThat(result).isEqualTo(50);
    }

    @Test
    @DisplayName("update should call jdbcTemplate.update with all params")
    void update_shouldReturnAffectedRows() {
        sampleProduct.setProductId(1);
        when(jdbcTemplate.update(anyString(),
                eq("Laptop"), eq("Gaming Laptop"), eq(1500.0),
                eq(2), eq("http://img/laptop.png"), eq(1)))
                .thenReturn(1);

        int result = productDao.update(sampleProduct);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("deleteById should remove product")
    void deleteById_shouldReturnAffectedRows() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        int result = productDao.deleteById(1);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("findById should return product when found")
    void findById_shouldReturnProduct() {
        Product expected = Product.builder().productId(1).productName("Laptop").price(1500.0).build();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(expected);

        Optional<Product> result = productDao.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getProductName()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("findById should return empty when not found")
    void findById_shouldReturnEmpty_whenNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(99)))
                .thenThrow(new EmptyResultDataAccessException(1));

        Optional<Product> result = productDao.findById(99);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll should return all products")
    void findAll_shouldReturnList() {
        List<Product> products = List.of(
                Product.builder().productId(1).productName("Laptop").price(1500.0).build(),
                Product.builder().productId(2).productName("Phone").price(800.0).build()
        );
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(products);

        List<Product> result = productDao.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findByCategoryId should return matching products")
    void findByCategoryId_shouldReturnList() {
        List<Product> products = List.of(sampleProduct);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(2))).thenReturn(products);

        List<Product> result = productDao.findByCategoryId(2);

        assertThat(result).hasSize(1);
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(2));
    }

    @Test
    @DisplayName("existsById should return true when product exists")
    void existsById_shouldReturnTrue() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1))).thenReturn(1);

        assertThat(productDao.existsById(1)).isTrue();
    }

    @Test
    @DisplayName("existsById should return false when product does not exist")
    void existsById_shouldReturnFalse() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(99))).thenReturn(0);

        assertThat(productDao.existsById(99)).isFalse();
    }
}
