package com.cts.ecommerce.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cts.ecommerce.entity.User;
import com.cts.ecommerce.exception.*;
import com.cts.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of {@link UserRepository}.
 * Provides CRUD operations and authentication-related queries
 * for {@link User} entities using {@link JdbcTemplate}.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** SQL query to insert a new user */
    private static final String INSERT_USER =
            "INSERT INTO Users(Name, Email, Password, PaymentDetails, Role) VALUES (?, ?, ?, ?, ?)";

    /** SQL query to retrieve all users */
    private static final String SELECT_ALL_USERS =
            "SELECT * FROM Users";

    /** SQL query to retrieve a user by ID */
    private static final String SELECT_USER_BY_ID =
            "SELECT * FROM Users WHERE UserId=?";

    /** SQL query to delete a user by ID */
    private static final String DELETE_USER_BY_ID =
            "DELETE FROM Users WHERE UserId=?";

    /** SQL query to retrieve password by email */
    private static final String SELECT_PASSWORD_BY_EMAIL =
            "SELECT password FROM Users WHERE email=?";

    /** SQL query to retrieve user by email */
    private static final String SELECT_USER_BY_EMAIL =
            "SELECT * FROM Users WHERE Email=?";

    /** SQL query to update user details */
    private static final String UPDATE_USER =
            "UPDATE Users SET Name = ?, Password = ?, PaymentDetails = ?, Role = ? WHERE UserId = ?";

    /**
     * RowMapper implementation for mapping ResultSet rows to {@link User} entities.
     */
    private final RowMapper<User> userRowMapper = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("UserId"));
            user.setName(rs.getString("Name"));
            user.setEmail(rs.getString("Email"));
            user.setPassword(rs.getString("Password"));
            user.setPaymentDetails(rs.getString("PaymentDetails"));
            user.setRole(rs.getString("Role"));
            return user;
        }
    };

    /**
     * Saves a new user to the database.
     *
     * @param user the {@link User} entity to be saved
     * @return number of rows affected
     */
    @Override
    public int save(User user) {
        try {
            return jdbcTemplate.update(INSERT_USER,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPaymentDetails(),
                    user.getRole());
        } catch (Exception ex) {
            throw new UserCreationException("Failed to create user");
        }
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of {@link User} entities
     */
    @Override
    public List<User> findAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL_USERS, userRowMapper);
        } catch (Exception ex) {
            throw new UserNotFoundException("Failed to fetch users");
        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return the {@link User} entity if found
     */
    @Override
    public User findById(int id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_ID, userRowMapper, id);
        } catch (Exception ex) {
            throw new UserNotFoundException("User not found with id " + id);
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return number of rows affected
     */
    @Override
    public int delete(int id) {
        try {
            return jdbcTemplate.update(DELETE_USER_BY_ID, id);
        } catch (Exception ex) {
            throw new UserDeletionException("Failed to delete user with id " + id);
        }
    }

    /**
     * Retrieves the password associated with an email.
     *
     * @param email the user email
     * @return password string
     */
    @Override
    public String getPassword(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    SELECT_PASSWORD_BY_EMAIL,
                    String.class,
                    email
            );
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Invalid email");
        }
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the user email
     * @return the {@link User} entity if found
     */
    @Override
    public User findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL, userRowMapper, email);
        } catch (Exception ex) {
            throw new UserNotFoundException("User not found with email " + email);
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id the user ID
     * @param existingUser the updated {@link User} data
     * @return number of rows affected
     */
    @Override
    public int update(int id, User existingUser) {
        try {
            return jdbcTemplate.update(
                    UPDATE_USER,
                    existingUser.getName(),
                    existingUser.getPassword(),
                    existingUser.getPaymentDetails(),
                    existingUser.getRole(),
                    id
            );
        } catch (Exception ex) {
            throw new UserUpdateException("Failed to update user with id " + id);
        }
    }
}