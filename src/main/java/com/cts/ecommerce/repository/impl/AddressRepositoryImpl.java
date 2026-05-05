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

    // SQL statements as constants
    private static final String SQL_INSERT = "INSERT INTO Address(UserId, HouseNo, Area, City, State, Country, Pincode) VALUES(?,?,?,?,?,?,?)";

    private static final String SQL_UPDATE = "UPDATE Address SET HouseNo=?, Area=?, City=?, State=?, Country=?, Pincode=? WHERE AddressId=?";

    private static final String SQL_DELETE = "DELETE FROM Address WHERE AddressId=?";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM Address WHERE AddressId=?";

    private static final String SQL_FIND_BY_USER = "SELECT * FROM Address WHERE UserId=?";

    private static final String SQL_FIND_ALL = "SELECT * FROM Address";

    private static final String SQL_FIND_RECENT_ID = "SELECT addressId FROM Address WHERE userId = ? ORDER BY addressId DESC LIMIT 1";

    @Override
    public int save(Address address) {
        return jdbcTemplate.update(SQL_INSERT,
                address.getUserId(), address.getHouseNo(), address.getArea(),
                address.getCity(), address.getState(), address.getCountry(), address.getPinCode());
    }

    @Override
    public int update(Address address) {
        return jdbcTemplate.update(SQL_UPDATE,
                address.getHouseNo(), address.getArea(), address.getCity(),
                address.getState(), address.getCountry(), address.getPinCode(), address.getAddressId());
    }

    @Override
    public int delete(int addressId) {
        return jdbcTemplate.update(SQL_DELETE, addressId);
    }

    @Override
    public Address findById(int addressId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,
                new BeanPropertyRowMapper<>(Address.class), addressId);
    }

    @Override
    public List<Address> findByUserId(int userId) {
        return jdbcTemplate.query(SQL_FIND_BY_USER,
                new BeanPropertyRowMapper<>(Address.class), userId);
    }

    @Override
    public List<Address> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL,
                new BeanPropertyRowMapper<>(Address.class));
    }

    @Override
    public int getIdOfRecentAddress(int userId) {
        Integer id = jdbcTemplate.queryForObject(SQL_FIND_RECENT_ID,
                new Object[]{userId}, Integer.class);
        return id == null ? -1 : id;
    }
}
