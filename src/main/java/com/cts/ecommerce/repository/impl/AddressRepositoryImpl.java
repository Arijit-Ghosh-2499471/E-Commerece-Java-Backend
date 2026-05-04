package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Address address) {
        String sql = "INSERT INTO Address(UserId, HouseNo, Area, City, State, Country, Pincode) VALUES(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, address.getUserId(), address.getHouseNo(), address.getArea(),
                address.getCity(), address.getState(), address.getCountry(), address.getPinCode());
    }

    @Override
    public int update(Address address) {
        String sql = "UPDATE Address SET HouseNo=?, Area=?, City=?, State=?, Country=?, Pincode=? WHERE AddressId=?";
        return jdbcTemplate.update(sql, address.getHouseNo(), address.getArea(), address.getCity(),
                address.getState(), address.getCountry(), address.getPinCode(), address.getAddressId());
    }

    @Override
    public int delete(int addressId) {
        String sql = "DELETE FROM Address WHERE AddressId=?";
        return jdbcTemplate.update(sql, addressId);
    }

    @Override
    public Address findById(int addressId) {
        String sql = "SELECT * FROM Address WHERE AddressId=?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Address.class), addressId);
    }

    @Override
    public List<Address> findByUserId(int userId) {
        String sql = "SELECT * FROM Address WHERE UserId=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Address.class), userId);
    }

    @Override
    public List<Address> findAll() {
        String sql = "SELECT * FROM Address";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Address.class));
    }

    @Override
    public int getIdOfAddress(int userId) {
        String sql = "SELECT addressId FROM Address WHERE userId = ? ORDER BY addressId DESC LIMIT 1";
        Integer id = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
        return id == null ? -1 : id;
    }

}
