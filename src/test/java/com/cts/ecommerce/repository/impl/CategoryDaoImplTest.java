package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Category;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CategoryRepositoryImpl categoryDao;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = Category.builder()
                .categoryName("Electronics")
                .build();
    }

    @Test
    @DisplayName("save should insert and populate generated id")
    void save_shouldInsertAndReturnId() {
        // Simulate KeyHolder being populated by jdbcTemplate
        doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 10));
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        int result = categoryDao.save(sampleCategory);

        assertThat(result).isEqualTo(10);
        assertThat(sampleCategory.getCategoryId()).isEqualTo(10);
    }

    @Test
    @DisplayName("update should call jdbcTemplate.update and return affected rows")
    void update_shouldReturnAffectedRows() {
        sampleCategory.setCategoryId(1);
        when(jdbcTemplate.update(anyString(), eq("Electronics"), eq(1))).thenReturn(1);

        int result = categoryDao.update(sampleCategory);

        assertThat(result).isEqualTo(1);
        verify(jdbcTemplate, times(1)).update(anyString(), eq("Electronics"), eq(1));
    }

    @Test
    @DisplayName("deleteById should call jdbcTemplate.update")
    void deleteById_shouldReturnAffectedRows() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        int result = categoryDao.deleteById(1);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("findById should return category when found")
    void findById_shouldReturnCategory() {
        Category expected = Category.builder().categoryId(1).categoryName("Electronics").build();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(expected);

        Optional<Category> result = categoryDao.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getCategoryName()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("findById should return empty when not found")
    void findById_shouldReturnEmpty_whenNotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(99)))
                .thenThrow(new EmptyResultDataAccessException(1));

        Optional<Category> result = categoryDao.findById(99);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll should return list of categories")
    void findAll_shouldReturnList() {
        List<Category> categories = List.of(
                Category.builder().categoryId(1).categoryName("Electronics").build(),
                Category.builder().categoryId(2).categoryName("Books").build()
        );
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(categories);

        List<Category> result = categoryDao.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("existsById should return true when count > 0")
    void existsById_shouldReturnTrue() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1))).thenReturn(1);

        assertThat(categoryDao.existsById(1)).isTrue();
    }

    @Test
    @DisplayName("existsById should return false when count is 0")
    void existsById_shouldReturnFalse() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(99))).thenReturn(0);

        assertThat(categoryDao.existsById(99)).isFalse();
    }

    @Test
    @DisplayName("existsById should return false when count is null")
    void existsById_shouldReturnFalse_whenNull() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(99))).thenReturn(null);

        assertThat(categoryDao.existsById(99)).isFalse();
    }
}