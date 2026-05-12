package com.cts.ecommerce.repository.impl;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.exception.*;
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

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AddressRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        try {
            return jdbcTemplate.update(SQL_INSERT,
                    address.getUserId(), address.getHouseNo(), address.getArea(),
                    address.getCity(), address.getState(), address.getCountry(), address.getPinCode());
        } catch (Exception ex) {
            throw new AddressCreationException("Failed to create address");
        }
    }

    /**
     * Updates an existing address in the database.
     *
     * @param address the {@link Address} entity with updated details
     * @return number of rows affected (0 if update failed)
     */
    @Override
    public int update(Address address) {
        try {
            return jdbcTemplate.update(SQL_UPDATE,
                    address.getHouseNo(), address.getArea(), address.getCity(),
                    address.getState(), address.getCountry(), address.getPinCode(), address.getAddressId());
        } catch (Exception ex) {
            throw new AddressUpdateException("Failed to update address with id " + address.getAddressId());
        }
    }

    /**
     * Deletes an address by its ID.
     *
     * @param addressId the ID of the address to delete
     * @return number of rows affected (0 if delete failed)
     */
    @Override
    public int delete(int addressId) {
        try {
            return jdbcTemplate.update(SQL_DELETE, addressId);
        } catch (Exception ex) {
            throw new AddressDeletionException("Failed to delete address with id " + addressId);
        }
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address
     * @return the {@link Address} entity if found
     */
    @Override
    public Address findById(int addressId) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,
                    new BeanPropertyRowMapper<>(Address.class), addressId);
        } catch (Exception ex) {
            throw new AddressNotFoundException("Address not found with id " + addressId);
        }
    }

    /**
     * Retrieves all addresses associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Address} entities for the user
     */
    @Override
    public List<Address> findByUserId(int userId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_USER,
                    new BeanPropertyRowMapper<>(Address.class), userId);
        } catch (Exception ex) {
            throw new AddressNotFoundException("Addresses not found for userId " + userId);
        }
    }

    /**
     * Retrieves all addresses in the system.
     *
     * @return list of all {@link Address} entities
     */
    @Override
    public List<Address> findAll() {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL,
                    new BeanPropertyRowMapper<>(Address.class));
        } catch (Exception ex) {
            throw new AddressNotFoundException("Failed to fetch addresses");
        }
    }

    /**
     * Retrieves the ID of the most recent address for a given user.
     *
     * @param userId the ID of the user
     * @return the ID of the most recent address, or -1 if none exists
     */
    @Override
    public int getIdOfRecentAddress(int userId) {
        try {
            Integer id = jdbcTemplate.queryForObject(SQL_FIND_RECENT_ID,
                    new Object[]{userId}, Integer.class);
            return id == null ? -1 : id;
        } catch (Exception ex) {
            throw new AddressNotFoundException("Recent address not found for userId " + userId);
        }
    }
}