package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Category;
import com.cts.ecommerce.exception.CategoryCreationException;
import com.cts.ecommerce.exception.CategoryDeletionException;
import com.cts.ecommerce.exception.CategoryNotFoundException;
import com.cts.ecommerce.exception.CategoryRetrievalException;
import com.cts.ecommerce.exception.CategoryUpdateException;
import com.cts.ecommerce.repository.CategoryRepository;
import com.cts.ecommerce.service.impl.CategoryServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CategoryServiceImpl}.
 * <p>
 * Uses Mockito to stub the {@link CategoryRepository} so the service can
 * be exercised without touching the database.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Electronics");
    }

    // ---------------------------------------------------------------
    //  createCategory
    // ---------------------------------------------------------------

    /**
     * Tests successful category creation when the repository accepts the insert.
     */
    @Test
    void createCategory_ShouldReturnCategory_WhenRepositorySucceeds() {
        when(categoryRepository.save(category)).thenReturn(1);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
    }

    /**
     * Tests that a CategoryCreationException raised by the repository is propagated.
     */
    @Test
    void createCategory_ShouldPropagateCreationException_WhenRepositoryThrows() {
        when(categoryRepository.save(category))
                .thenThrow(new CategoryCreationException("DB constraint violation"));

        CategoryCreationException ex = assertThrows(
                CategoryCreationException.class,
                () -> categoryService.createCategory(category)
        );

        assertTrue(ex.getMessage().contains("DB constraint violation"));
        verify(categoryRepository, times(1)).save(category);
    }

    /**
     * Tests that an unexpected RuntimeException is wrapped as CategoryCreationException.
     */
    @Test
    void createCategory_ShouldWrapUnexpectedException_AsCreationException() {
        when(categoryRepository.save(category))
                .thenThrow(new RuntimeException("Unexpected DB error"));

        CategoryCreationException ex = assertThrows(
                CategoryCreationException.class,
                () -> categoryService.createCategory(category)
        );

        assertTrue(ex.getMessage().contains("Error occurred while creating category"));
        verify(categoryRepository, times(1)).save(category);
    }

    // ---------------------------------------------------------------
    //  updateCategory
    // ---------------------------------------------------------------

    /**
     * Tests successful category update when the target exists.
     */
    @Test
    void updateCategory_ShouldReturnUpdatedCategory_WhenCategoryExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.update(any(Category.class))).thenReturn(1);

        Category result = categoryService.updateCategory(1, category);

        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).existsById(1);
        verify(categoryRepository, times(1)).update(category);
    }

    /**
     * Tests CategoryNotFoundException when updating a category that does not exist.
     */
    @Test
    void updateCategory_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.updateCategory(99, category)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(categoryRepository, times(1)).existsById(99);
        verify(categoryRepository, never()).update(any(Category.class));
    }

    /**
     * Tests that a CategoryUpdateException raised by the repository is propagated.
     */
    @Test
    void updateCategory_ShouldPropagateUpdateException_WhenRepositoryThrows() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.update(any(Category.class)))
                .thenThrow(new CategoryUpdateException("DB error"));

        CategoryUpdateException ex = assertThrows(
                CategoryUpdateException.class,
                () -> categoryService.updateCategory(1, category)
        );

        assertTrue(ex.getMessage().contains("DB error"));
        verify(categoryRepository, times(1)).update(any(Category.class));
    }

    // ---------------------------------------------------------------
    //  deleteCategory
    // ---------------------------------------------------------------

    /**
     * Tests successful category deletion when the target exists.
     */
    @Test
    void deleteCategory_ShouldDeleteCategory_WhenCategoryExists() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.deleteById(1)).thenReturn(1);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1));

        verify(categoryRepository, times(1)).existsById(1);
        verify(categoryRepository, times(1)).deleteById(1);
    }

    /**
     * Tests CategoryNotFoundException when deleting a category that does not exist.
     */
    @Test
    void deleteCategory_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(99)).thenReturn(false);

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(99)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(categoryRepository, times(1)).existsById(99);
        verify(categoryRepository, never()).deleteById(anyInt());
    }

    /**
     * Tests that a CategoryDeletionException raised by the repository is propagated
     * (e.g. due to a foreign-key constraint).
     */
    @Test
    void deleteCategory_ShouldPropagateDeletionException_WhenRepositoryThrows() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.deleteById(1))
                .thenThrow(new CategoryDeletionException("FK constraint"));

        CategoryDeletionException ex = assertThrows(
                CategoryDeletionException.class,
                () -> categoryService.deleteCategory(1)
        );

        assertTrue(ex.getMessage().contains("FK constraint"));
        verify(categoryRepository, times(1)).deleteById(1);
    }

    // ---------------------------------------------------------------
    //  getCategoryById
    // ---------------------------------------------------------------

    /**
     * Tests fetching an existing category by id.
     */
    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        when(categoryRepository.findById(1)).thenReturn(category);

        Category result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).findById(1);
    }

    /**
     * Tests CategoryNotFoundException when fetching a category that does not exist.
     */
    @Test
    void getCategoryById_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(99))
                .thenThrow(new CategoryNotFoundException("Category not found with id 99"));

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(99)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(categoryRepository, times(1)).findById(99);
    }

    /**
     * Tests that an unexpected RuntimeException is wrapped as CategoryNotFoundException.
     */
    @Test
    void getCategoryById_ShouldWrapUnexpectedException_AsNotFoundException() {
        when(categoryRepository.findById(1))
                .thenThrow(new RuntimeException("Unexpected DB error"));

        CategoryNotFoundException ex = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(1)
        );

        assertTrue(ex.getMessage().contains("Error occurred while fetching category"));
        verify(categoryRepository, times(1)).findById(1);
    }

    // ---------------------------------------------------------------
    //  getAllCategories
    // ---------------------------------------------------------------

    /**
     * Tests fetching all categories when results are present.
     */
    @Test
    void getAllCategories_ShouldReturnList_WhenCategoriesExist() {
        Category second = new Category();
        second.setCategoryId(2);
        second.setCategoryName("Books");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category, second));

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    /**
     * Tests fetching all categories when none exist (empty list, not null).
     */
    @Test
    void getAllCategories_ShouldReturnEmptyList_WhenNoCategoriesExist() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    /**
     * Tests that a CategoryRetrievalException raised by the repository is propagated.
     */
    @Test
    void getAllCategories_ShouldPropagateRetrievalException_WhenRepositoryThrows() {
        when(categoryRepository.findAll())
                .thenThrow(new CategoryRetrievalException("DB error"));

        CategoryRetrievalException ex = assertThrows(
                CategoryRetrievalException.class,
                () -> categoryService.getAllCategories()
        );

        assertTrue(ex.getMessage().contains("DB error"));
        verify(categoryRepository, times(1)).findAll();
    }
}