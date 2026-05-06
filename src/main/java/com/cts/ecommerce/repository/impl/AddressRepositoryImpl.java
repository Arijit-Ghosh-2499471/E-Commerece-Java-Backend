package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC-based implementation of {@link AddressRepository}.
 * Provides CRUD operations and query methods for {@link Address} entities
 * using {@link JdbcTemplate}.
 */
@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // SQL statements as constants
    /** SQL query to insert a new address */
    private static final String SQL_INSERT =
            "INSERT INTO Address(UserId, HouseNo, Area, City, State, Country, Pincode) VALUES(?,?,?,?,?,?,?)";

    /** SQL query to update an existing address */
    private static final String SQL_UPDATE =
            "UPDATE Address SET HouseNo=?, Area=?, City=?, State=?, Country=?, Pincode=? WHERE AddressId=?";

    /** SQL query to delete an address by ID */
    private static final String SQL_DELETE =
            "DELETE FROM Address WHERE AddressId=?";

    /** SQL query to find an address by ID */
    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM Address WHERE AddressId=?";

    /** SQL query to find addresses by user ID */
    private static final String SQL_FIND_BY_USER =
            "SELECT * FROM Address WHERE UserId=?";

    /** SQL query to retrieve all addresses */
    private static final String SQL_FIND_ALL =
            "SELECT * FROM Address";

    /** SQL query to get the most recent address ID for a user */
    private static final String SQL_FIND_RECENT_ID =
            "SELECT addressId FROM Address WHERE userId = ? ORDER BY addressId DESC LIMIT 1";

    /**
     * Saves a new address to the database.
     *
     * @param address the {@link Address} entity to be saved
     * @return number of rows affected (0 if insert failed)
     */
    @Override
    public int save(Address address) {
        return jdbcTemplate.update(SQL_INSERT,
                address.getUserId(), address.getHouseNo(), address.getArea(),
                address.getCity(), address.getState(), address.getCountry(), address.getPinCode());
    }

    /**
     * Updates an existing address in the database.
     *
     * @param address the {@link Address} entity with updated details
     * @return number of rows affected (0 if update failed)
     */
    @Override
    public int update(Address address) {
        return jdbcTemplate.update(SQL_UPDATE,
                address.getHouseNo(), address.getArea(), address.getCity(),
                address.getState(), address.getCountry(), address.getPinCode(), address.getAddressId());
    }

    /**
     * Deletes an address by its ID.
     *
     * @param addressId the ID of the address to delete
     * @return number of rows affected (0 if delete failed)
     */
    @Override
    public int delete(int addressId) {
        return jdbcTemplate.update(SQL_DELETE, addressId);
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address
     * @return the {@link Address} entity if found
     */
    @Override
    public Address findById(int addressId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,
                new BeanPropertyRowMapper<>(Address.class), addressId);
    }

    /**
     * Retrieves all addresses associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Address} entities for the user
     */
    @Override
    public List<Address> findByUserId(int userId) {
        return jdbcTemplate.query(SQL_FIND_BY_USER,
                new BeanPropertyRowMapper<>(Address.class), userId);
    }

    /**
     * Retrieves all addresses in the system.
     *
     * @return list of all {@link Address} entities
     */
    @Override
    public List<Address> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL,
                new BeanPropertyRowMapper<>(Address.class));
    }

    /**
     * Retrieves the ID of the most recent address for a given user.
     *
     * @param userId the ID of the user
     * @return the ID of the most recent address, or -1 if none exists
     */
    @Override
    public int getIdOfRecentAddress(int userId) {
        Integer id = jdbcTemplate.queryForObject(SQL_FIND_RECENT_ID,
                new Object[]{userId}, Integer.class);
        return id == null ? -1 : id;
    }
}
