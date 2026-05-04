package com.cts.ecommerce.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cts.ecommerce.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cts.ecommerce.entity.Users;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ RowMapper INSIDE repository
    private RowMapper<Users> userRowMapper = new RowMapper<Users>() {
        @Override
        public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
            Users user = new Users();
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
    public int save(Users user) {
        String sql = "INSERT INTO Users(Name, Email, Password, PaymentDetails, Role) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPaymentDetails(),
                user.getRole());
    }

    @Override
    public List<Users> findAll() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public Users findById(int id) {
        String sql = "SELECT * FROM Users WHERE UserId=?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM Users WHERE UserId=?";
        return jdbcTemplate.update(sql, id);
    }
}