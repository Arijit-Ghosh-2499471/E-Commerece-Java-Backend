package com.cts.ecommerce.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cts.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cts.ecommerce.entity.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ SQL constants at the top
    private static final String INSERT_USER =
            "INSERT INTO Users(Name, Email, Password, PaymentDetails, Role) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_USERS =
            "SELECT * FROM Users";

    private static final String SELECT_USER_BY_ID =
            "SELECT * FROM Users WHERE UserId=?";

    private static final String DELETE_USER_BY_ID =
            "DELETE FROM Users WHERE UserId=?";

    private static final String SELECT_PASSWORD_BY_EMAIL =
            "SELECT password FROM Users WHERE email=?";

    private static final String SELECT_USER_BY_EMAIL =
            "SELECT * FROM Users WHERE Email=?";

    private static final String UPDATE_USER =
            "UPDATE Users SET Name = ?, Password = ?, PaymentDetails = ?, Role = ? WHERE UserId = ?";


    // ✅ RowMapper INSIDE repository
    private RowMapper<User> userRowMapper = new RowMapper<User>() {

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

    @Override
    public int save(User user) {
        return jdbcTemplate.update(INSERT_USER,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPaymentDetails(),
                user.getRole());
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS, userRowMapper);
    }

    @Override
    public User findById(int id) {
        return jdbcTemplate.queryForObject(SELECT_USER_BY_ID, userRowMapper, id);
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(DELETE_USER_BY_ID, id);
    }

    @Override
    public String getPassword(String email) {
        return jdbcTemplate.queryForObject(SELECT_PASSWORD_BY_EMAIL, String.class, email);
    }

    @Override
    public User findByEmail(String email) {
        return jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL, userRowMapper, email);
    }

    @Override
    public int update(int id, User existingUser) {
        return jdbcTemplate.update(
                UPDATE_USER,
                existingUser.getName(),
                existingUser.getPassword(),
                existingUser.getPaymentDetails(),
                existingUser.getRole(),
                id
        );
    }
}