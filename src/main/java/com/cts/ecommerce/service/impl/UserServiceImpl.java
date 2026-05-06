package com.cts.ecommerce.service.impl;

import java.util.List;

import com.cts.ecommerce.entity.User;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.repository.UserRepository;
import com.cts.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Implementation of {@link UserService} that provides user management
 * and authentication-related operations.
 * <p>
 * This service handles user creation, retrieval, update, deletion,
 * login validation, and registration while enforcing business rules
 * and throwing domain-specific custom exceptions.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    /**
     * Constructor-based dependency injection.
     *
     * @param userRepository user repository
     * @param shoppingCartRepository shopping cart repository
     */
    public UserServiceImpl(UserRepository userRepository,
                           ShoppingCartRepository shoppingCartRepository) {
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
        logger.info("Creating user with email={}", user.getEmail());
        try {
            return userRepository.save(user);
        } catch (Exception ex) {
            logger.error("Failed to create user with email={}", user.getEmail(), ex);
            throw new UserCreationException(
                    "Unable to create user with email: " + user.getEmail());
        }
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users
     * @throws UserNotFoundException if users cannot be fetched
     */
    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        try {
            return userRepository.findAll();
        } catch (Exception ex) {
            logger.error("Failed to fetch users", ex);
            throw new UserNotFoundException("Unable to fetch users");
        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id user ID
     * @return the {@link User}
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public User getUserById(int id) {
        logger.info("Fetching user with ID={}", id);
        try {
            User user = userRepository.findById(id);
            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }
            return user;
        } catch (UserNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error fetching user with ID={}", id, ex);
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id user ID
     * @return number of rows affected
     * @throws UserNotFoundException if user does not exist
     * @throws UserDeletionException if deletion fails
     */
    @Override
    public int deleteUser(int id) {
        logger.info("Deleting user with ID={}", id);
        try {
            int rows = userRepository.delete(id);
            if (rows == 0) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }
            return rows;
        } catch (UserNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Failed to delete user with ID={}", id, ex);
            throw new UserDeletionException(
                    "Unable to delete user with ID: " + id);
        }
    }

    /**
     * Validates user login credentials.
     *
     * @param email user email
     * @param password user password
     * @return true if credentials are valid, false otherwise
     * @throws AuthenticationFailedException if authentication fails
     */
    @Override
    public boolean login(String email, String password) {
        logger.info("Login attempt for email={}", email);
        try {
            String storedPassword = userRepository.getPassword(email);
            boolean valid = storedPassword.equals(hashPassword(password));

            if (valid) {
                logger.info("Login successful for email={}", email);
            } else {
                logger.warn("Login failed for email={}", email);
            }

            return valid;
        } catch (Exception ex) {
            logger.warn("Authentication failed for email={}", email);
            throw new AuthenticationFailedException("Invalid email or password");
        }
    }

    /**
     * Registers a new user and creates an initial shopping cart.
     *
     * @param user user to be registered
     * @throws UserCreationException if registration fails
     */
    @Override
    public void register(User user) {
        logger.debug("Registering user with email={}", user.getEmail());
        try {
            String hashedPassword = hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            userRepository.save(user);

            User savedUser = userRepository.findByEmail(user.getEmail());
            shoppingCartRepository.createCart(savedUser.getUserId());

            logger.info("User registered successfully with userId={}",
                    savedUser.getUserId());
        } catch (Exception ex) {
            logger.error("Registration failed for email={}", user.getEmail(), ex);
            throw new UserCreationException(
                    "Unable to register user with email: " + user.getEmail());
        }
    }

    /**
     * Retrieves a user by email.
     *
     * @param email user email
     * @return the {@link User}
     * @throws UserNotFoundException if user is not found
     */
    @Override
    public User getUserByEmail(String email) {
        logger.debug("Fetching user with email={}", email);
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UserNotFoundException(
                        "User not found with email: " + email);
            }
            return user;
        } catch (UserNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error fetching user with email={}", email, ex);
            throw new UserNotFoundException(
                    "User not found with email: " + email);
        }
    }

    /**
     * Updates user details.
     *
     * @param id user ID
     * @param user updated user data
     * @return number of rows affected
     * @throws UserNotFoundException if user does not exist
     * @throws UserUpdateException if update fails
     */
    @Override
    public int updateUser(int id, User user) {
        logger.debug("Updating user with ID={}", id);
        try {
            User existingUser = userRepository.findById(id);
            if (existingUser == null) {
                throw new UserNotFoundException("User not found with ID: " + id);
            }

            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(hashPassword(user.getPassword()));
            existingUser.setPaymentDetails(user.getPaymentDetails());
            existingUser.setRole(user.getRole());

            int rows = userRepository.update(id, existingUser);
            if (rows == 0) {
                throw new UserUpdateException(
                        "User update failed for ID: " + id);
            }

            logger.info("User updated successfully with ID={}", id);
            return rows;
        } catch (UserNotFoundException | UserUpdateException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Failed to update user with ID={}", id, ex);
            throw new UserUpdateException(
                    "Unable to update user with ID: " + id);
        }
    }

    /**
     * Generates SHA-256 hash for the given password.
     *
     * @param password the plain text password to be hashed
     * @return the SHA-256 hashed password in hexadecimal format
     * @throws UserCreationException if the hashing algorithm is not available
     */
    private String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashedBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new UserCreationException("Password hashing failed");
        }
    }
}