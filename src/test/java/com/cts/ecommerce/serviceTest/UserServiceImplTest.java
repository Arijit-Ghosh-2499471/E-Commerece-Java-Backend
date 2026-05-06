package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.User;
import com.cts.ecommerce.exception.AuthenticationFailedException;
import com.cts.ecommerce.exception.UserCreationException;
import com.cts.ecommerce.exception.UserDeletionException;
import com.cts.ecommerce.exception.UserNotFoundException;
import com.cts.ecommerce.exception.UserUpdateException;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.repository.UserRepository;
import com.cts.ecommerce.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setPaymentDetails("UPI");
        user.setRole("Customer");
    }

    /**
     * Tests successful user creation.
     */
    @Test
    void createUser_ShouldReturnOne_WhenUserIsCreatedSuccessfully() {
        when(userRepository.save(user)).thenReturn(1);

        int result = userService.createUser(user);

        assertEquals(1, result);
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Tests UserCreationException when user creation fails.
     */
    @Test
    void createUser_ShouldThrowUserCreationException_WhenRepositoryFails() {
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));

        UserCreationException exception = assertThrows(
                UserCreationException.class,
                () -> userService.createUser(user)
        );

        assertTrue(exception.getMessage().contains("Unable to create user"));
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Tests fetching all users successfully.
     */
    @Test
    void getAllUsers_ShouldReturnUserList_WhenUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("test@example.com", users.getFirst().getEmail());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Tests UserNotFoundException when fetching all users fails.
     */
    @Test
    void getAllUsers_ShouldThrowUserNotFoundException_WhenRepositoryFails() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getAllUsers()
        );

        assertTrue(exception.getMessage().contains("Unable to fetch users"));
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Tests fetching user by ID successfully.
     */
    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1)).thenReturn(user);

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("Test User", result.getName());
        verify(userRepository, times(1)).findById(1);
    }

    /**
     * Tests UserNotFoundException when user by ID returns null.
     */
    @Test
    void getUserById_ShouldThrowUserNotFoundException_WhenUserIsNull() {
        when(userRepository.findById(1)).thenReturn(null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(1)
        );

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).findById(1);
    }

    /**
     * Tests UserNotFoundException when repository throws exception while fetching user by ID.
     */
    @Test
    void getUserById_ShouldThrowUserNotFoundException_WhenRepositoryFails() {
        when(userRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(1)
        );

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).findById(1);
    }

    /**
     * Tests successful user deletion.
     */
    @Test
    void deleteUser_ShouldReturnOne_WhenUserIsDeletedSuccessfully() {
        when(userRepository.delete(1)).thenReturn(1);

        int result = userService.deleteUser(1);

        assertEquals(1, result);
        verify(userRepository, times(1)).delete(1);
    }

    /**
     * Tests UserNotFoundException when delete returns zero rows.
     */
    @Test
    void deleteUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.delete(1)).thenReturn(0);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(1)
        );

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).delete(1);
    }

    /**
     * Tests UserDeletionException when repository fails during deletion.
     */
    @Test
    void deleteUser_ShouldThrowUserDeletionException_WhenRepositoryFails() {
        when(userRepository.delete(1)).thenThrow(new RuntimeException("Database error"));

        UserDeletionException exception = assertThrows(
                UserDeletionException.class,
                () -> userService.deleteUser(1)
        );

        assertTrue(exception.getMessage().contains("Unable to delete user"));
        verify(userRepository, times(1)).delete(1);
    }

    /**
     * Tests successful login with valid credentials.
     */
    @Test
    void login_ShouldReturnTrue_WhenCredentialsAreValid() {
        when(userRepository.getPassword("test@example.com")).thenReturn("password123");

        boolean result = userService.login("test@example.com", "password123");

        assertTrue(result);
        verify(userRepository, times(1)).getPassword("test@example.com");
    }

    /**
     * Tests login failure with invalid password.
     */
    @Test
    void login_ShouldReturnFalse_WhenPasswordIsInvalid() {
        when(userRepository.getPassword("test@example.com")).thenReturn("password123");

        boolean result = userService.login("test@example.com", "wrongpassword");

        assertFalse(result);
        verify(userRepository, times(1)).getPassword("test@example.com");
    }

    /**
     * Tests AuthenticationFailedException when login repository call fails.
     */
    @Test
    void login_ShouldThrowAuthenticationFailedException_WhenRepositoryFails() {
        when(userRepository.getPassword("test@example.com"))
                .thenThrow(new RuntimeException("Database error"));

        AuthenticationFailedException exception = assertThrows(
                AuthenticationFailedException.class,
                () -> userService.login("test@example.com", "password123")
        );

        assertTrue(exception.getMessage().contains("Invalid email or password"));
        verify(userRepository, times(1)).getPassword("test@example.com");
    }

    /**
     * Tests successful user registration.
     */
    @Test
    void register_ShouldSaveUserAndCreateCart_WhenRegistrationIsSuccessful() {
        when(userRepository.save(user)).thenReturn(1);
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        userService.register(user);

        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(shoppingCartRepository, times(1)).createCart(1);
    }

    /**
     * Tests UserCreationException when registration fails.
     */
    @Test
    void register_ShouldThrowUserCreationException_WhenRepositoryFails() {
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));

        UserCreationException exception = assertThrows(
                UserCreationException.class,
                () -> userService.register(user)
        );

        assertTrue(exception.getMessage().contains("Unable to register user"));
        verify(userRepository, times(1)).save(user);
        verify(userRepository, never()).findByEmail(anyString());
        verify(shoppingCartRepository, never()).createCart(anyInt());
    }

    /**
     * Tests fetching user by email successfully.
     */
    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    /**
     * Tests UserNotFoundException when user by email is null.
     */
    @Test
    void getUserByEmail_ShouldThrowUserNotFoundException_WhenUserIsNull() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByEmail("test@example.com")
        );

        assertTrue(exception.getMessage().contains("User not found with email"));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    /**
     * Tests successful user update.
     */
    @Test
    void updateUser_ShouldReturnOne_WhenUserIsUpdatedSuccessfully() {
        User updatedUser = new User();
        updatedUser.setUserId(1);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setPaymentDetails("Credit Card");
        updatedUser.setRole("Customer");

        when(userRepository.findById(1)).thenReturn(user);
        when(userRepository.update(eq(1), any(User.class))).thenReturn(1);

        int result = userService.updateUser(1, updatedUser);

        assertEquals(1, result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).update(eq(1), any(User.class));
    }

    /**
     * Tests UserNotFoundException when updating non-existing user.
     */
    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1)).thenReturn(null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(1, user)
        );

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).update(anyInt(), any(User.class));
    }

    /**
     * Tests UserUpdateException when update returns zero rows.
     */
    @Test
    void updateUser_ShouldThrowUserUpdateException_WhenUpdateReturnsZero() {
        when(userRepository.findById(1)).thenReturn(user);
        when(userRepository.update(eq(1), any(User.class))).thenReturn(0);

        UserUpdateException exception = assertThrows(
                UserUpdateException.class,
                () -> userService.updateUser(1, user)
        );

        assertTrue(exception.getMessage().contains("User update failed"));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).update(eq(1), any(User.class));
    }

    /**
     * Tests UserUpdateException when repository fails during update.
     */
    @Test
    void updateUser_ShouldThrowUserUpdateException_WhenRepositoryFails() {
        when(userRepository.findById(1)).thenReturn(user);
        when(userRepository.update(eq(1), any(User.class)))
                .thenThrow(new RuntimeException("Database error"));

        UserUpdateException exception = assertThrows(
                UserUpdateException.class,
                () -> userService.updateUser(1, user)
        );

        assertTrue(exception.getMessage().contains("Unable to update user"));
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).update(eq(1), any(User.class));
    }
}