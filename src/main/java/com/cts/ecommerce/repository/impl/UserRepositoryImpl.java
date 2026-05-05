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
        String sql = "INSERT INTO Users(Name, Email, Password, PaymentDetails, Role) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPaymentDetails(),
                user.getRole());
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM Users WHERE UserId=?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM Users WHERE UserId=?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public String getPassword(String email) {
        String sql = "SELECT password FROM Users WHERE email=?";
        return jdbcTemplate.queryForObject(sql, String.class, email);
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email=?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, email);
    }

    @Override
    public int update(int id, User existingUser) {

        String sql = "UPDATE Users SET Name = ?, Password = ?, PaymentDetails = ?, Role = ? WHERE UserId = ?";

        return jdbcTemplate.update(
                sql,
                existingUser.getName(),
                existingUser.getPassword(),
                existingUser.getPaymentDetails(),
                existingUser.getRole(),
                id
        );
    }

}