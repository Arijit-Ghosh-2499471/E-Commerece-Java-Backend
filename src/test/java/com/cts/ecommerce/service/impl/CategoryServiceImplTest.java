package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.exception.ResourceNotFoundException;
import com.cts.ecommerce.model.Category;
import com.cts.ecommerce.repository.CategoryRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = Category.builder()
                .categoryId(1)
                .categoryName("Electronics")
                .build();
    }

    @Test
    @DisplayName("createCategory should save and return the category")
    void createCategory_shouldSaveAndReturnCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(1);

        Category result = categoryService.createCategory(sampleCategory);

        assertThat(result).isNotNull();
        assertThat(result.getCategoryName()).isEqualTo("Electronics");
        verify(categoryRepository, times(1)).save(sampleCategory);
    }

    @Test
    @DisplayName("updateCategory should update when category exists")
    void updateCategory_shouldUpdate_whenExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.update(any(Category.class))).thenReturn(1);

        Category updated = Category.builder().categoryName("Books").build();
        Category result = categoryService.updateCategory(1, updated);

        assertThat(result.getCategoryId()).isEqualTo(1);
        assertThat(result.getCategoryName()).isEqualTo("Books");
        verify(categoryRepository).update(updated);
    }

    @Test
    @DisplayName("updateCategory should throw when category not found")
    void updateCategory_shouldThrow_whenNotFound() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.updateCategory(99, sampleCategory))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with id: 99");

        verify(categoryRepository, never()).update(any());
    }

    @Test
    @DisplayName("deleteCategory should remove when exists")
    void deleteCategory_shouldDelete_whenExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.deleteById(1)).thenReturn(1);

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteCategory should throw when not found")
    void deleteCategory_shouldThrow_whenNotFound() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.deleteCategory(99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("getCategoryById should return category when found")
    void getCategoryById_shouldReturn_whenFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(sampleCategory));

        Category result = categoryService.getCategoryById(1);

        assertThat(result).isEqualTo(sampleCategory);
    }

    @Test
    @DisplayName("getCategoryById should throw when not found")
    void getCategoryById_shouldThrow_whenNotFound() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with id: 99");
    }

    @Test
    @DisplayName("getAllCategories should return list of categories")
    void getAllCategories_shouldReturnList() {
        Category another = Category.builder().categoryId(2).categoryName("Books").build();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(sampleCategory, another));

        List<Category> result = categoryService.getAllCategories();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Category::getCategoryName)
                .containsExactly("Electronics", "Books");
    }

    @Test
    @DisplayName("getAllCategories should return empty list when no categories")
    void getAllCategories_shouldReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = categoryService.getAllCategories();

        assertThat(result).isEmpty();
    }
}