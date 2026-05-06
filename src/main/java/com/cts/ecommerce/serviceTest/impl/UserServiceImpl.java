package com.cts.ecommerce.serviceTest.impl;

import java.util.List;

import com.cts.ecommerce.exception.AuthenticationFailedException;
import com.cts.ecommerce.exception.UserCreationException;
import com.cts.ecommerce.exception.UserDeletionException;
import com.cts.ecommerce.exception.UserNotFoundException;
import com.cts.ecommerce.exception.UserUpdateException;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.serviceTest.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cts.ecommerce.entity.User;
import com.cts.ecommerce.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    /**
     * Constructor-based dependency injection.
     *
     * @param userRepository user repository object
     * @param shoppingCartRepository shopping cart repository object
     */
    public UserServiceImpl(UserRepository userRepository, ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    /**
     * Creates a new user.
     *
     * @param user user object to be created
     * @return number of rows affected
     * @throws UserCreationException if user creation fails
     */
    @Override
    public int createUser(User user) {
        try {
            log.info("Creating user with email: {}", user.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Failed to create user with email: {}", user.getEmail(), e);
            throw new UserCreationException("Unable to create user with email: " + user.getEmail(), e);
        }
    }

    /**
     * Fetch all users.
     *
     * @return list of all users
     * @throws UserNotFoundException if users cannot be fetched
     */
    @Override
    public List<User> getAllUsers() {
        try {
            log.info("Fetching all users");
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to fetch all users", e);
            throw new UserNotFoundException("Unable to fetch users", e);
        }
    }

    /**
     * Fetch user by ID.
     *
     * @param id user ID
     * @return user object
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public User getUserById(int id) {
        try {
            log.info("Fetching user with ID: {}", id);

            User user = userRepository.findById(id);

            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            return user;

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch user with ID: {}", id, e);
            throw new UserNotFoundException("User not found with ID: " + id, e);
        }
    }

    /**
     * Deletes user by ID.
     *
     * @param id user ID
     * @return number of rows affected
     * @throws UserDeletionException if user deletion fails
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public int deleteUser(int id) {
        try {
            log.info("Deleting user with ID: {}", id);

            int result = userRepository.delete(id);

            if (result == 0) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            return result;

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete user with ID: {}", id, e);
            throw new UserDeletionException("Unable to delete user with ID: " + id, e);
        }
    }

    /**
     * Validates login credentials.
     *
     * @param email user email
     * @param password user password
     * @return true if login credentials are valid, otherwise false
     * @throws AuthenticationFailedException if authentication process fails
     */
    @Override
    public boolean login(String email, String password) {
        try {
            log.info("Login attempt for email: {}", email);

            String storedPassword = userRepository.getPassword(email);

            boolean isValid = storedPassword.equals(password);

            if (isValid) {
                log.info("Login successful for email: {}", email);
            } else {
                log.warn("Login failed for email: {}", email);
            }

            return isValid;

        } catch (Exception e) {
            log.warn("Login failed for email: {}", email, e);
            throw new AuthenticationFailedException("Invalid email or password", e);
        }
    }

    /**
     * Registers a new user and creates a shopping cart.
     *
     * @param user user object to be registered
     * @throws UserCreationException if registration or cart creation fails
     */
    @Override
    public void register(User user) {
        try {
            log.info("Registering new user: {}", user.getEmail());

            userRepository.save(user);

            User savedUser = userRepository.findByEmail(user.getEmail());

            shoppingCartRepository.createCart(savedUser.getUserId());

            log.info("User registered successfully with ID: {}", savedUser.getUserId());

        } catch (Exception e) {
            log.error("Failed to register user with email: {}", user.getEmail(), e);
            throw new UserCreationException("Unable to register user with email: " + user.getEmail(), e);
        }
    }

    /**
     * Fetch user by email.
     *
     * @param email user email
     * @return user object
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public User getUserByEmail(String email) {
        try {
            log.info("Fetching user with email: {}", email);

            User user = userRepository.findByEmail(email);

            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + email);
            }

            return user;

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch user with email: {}", email, e);
            throw new UserNotFoundException("User not found with email: " + email, e);
        }
    }

    /**
     * Updates user details.
     *
     * @param id user ID
     * @param user updated user object
     * @return number of rows affected
     * @throws UserNotFoundException if user is not found
     * @throws UserUpdateException if user update fails
     */
    @Override
    public int updateUser(int id, User user) {
        try {
            log.info("Updating user with ID: {}", id);

            User existingUser = userRepository.findById(id);

            if (existingUser == null) {
                log.warn("User not found for update: {}", id);
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPaymentDetails(user.getPaymentDetails());
            existingUser.setRole(user.getRole());

            int result = userRepository.update(id, existingUser);

            if (result == 0) {
                throw new UserUpdateException("User update failed for ID: " + id);
            }

            log.info("User updated successfully: {}", id);

            return result;

        } catch (UserNotFoundException | UserUpdateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update user with ID: {}", id, e);
            throw new UserUpdateException("Unable to update user with ID: " + id, e);
        }
    }
}