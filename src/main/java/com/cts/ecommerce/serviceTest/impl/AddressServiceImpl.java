package com.cts.ecommerce.serviceTest.impl;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.exception.AddressNotFoundException;
import com.cts.ecommerce.exception.AddressCreationException;
import com.cts.ecommerce.exception.AddressUpdateException;
import com.cts.ecommerce.exception.AddressDeletionException;
import com.cts.ecommerce.repository.AddressRepository;
import com.cts.ecommerce.serviceTest.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of {@link AddressService} that provides CRUD operations
 * for {@link Address} entities using a JDBC-based {@link AddressRepository}.
 * <p>
 * Each method validates repository results and throws custom exceptions
 * when operations fail, ensuring robust error handling.
 */
@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressRepository addressRepository;

    /**
     * Adds a new address to the database.
     *
     * @param address the {@link Address} entity to be saved
     * @throws AddressCreationException if the insert fails
     */
    @Override
    public void addAddress(Address address) {
        logger.info("Attempting to add address for userId={}", address.getUserId());
        int rows = addressRepository.save(address);
        if (rows == 0) {
            logger.error("Failed to insert address for userId={}", address.getUserId());
            throw new AddressCreationException("Failed to insert address for userId: " + address.getUserId());
        }
        logger.info("Successfully added address for userId={}", address.getUserId());
    }

    /**
     * Retrieves the ID of the most recent address for a given user.
     *
     * @param userId the ID of the user
     * @return the ID of the most recent address
     * @throws AddressNotFoundException if no address exists
     */
    @Override
    public int getIdOfAddress(int userId) {
        logger.debug("Fetching recent address ID for userId={}", userId);
        int id = addressRepository.getIdOfRecentAddress(userId);
        if (id == -1) {
            logger.warn("No recent address found for userId={}", userId);
            throw new AddressNotFoundException("No recent address found for userId: " + userId);
        }
        logger.info("Found recent addressId={} for userId={}", id, userId);
        return id;
    }

    /**
     * Updates an existing address in the database.
     *
     * @param address the {@link Address} entity with updated details
     * @return number of rows affected
     * @throws AddressUpdateException if update fails
     */
    @Override
    public int updateAddress(Address address) {
        logger.info("Attempting to update addressId={}", address.getAddressId());
        int rows = addressRepository.update(address);
        if (rows == 0) {
            logger.error("Failed to update addressId={}", address.getAddressId());
            throw new AddressUpdateException("Failed to update address with id: " + address.getAddressId());
        }
        logger.info("Successfully updated addressId={}", address.getAddressId());
        return rows;
    }

    /**
     * Deletes an address by its ID.
     *
     * @param addressId the ID of the address to delete
     * @throws AddressDeletionException if deletion fails
     */
    @Override
    public void deleteAddress(int addressId) {
        logger.info("Attempting to delete addressId={}", addressId);
        int rows = addressRepository.delete(addressId);
        if (rows == 0) {
            logger.error("Failed to delete addressId={}", addressId);
            throw new AddressDeletionException("Failed to delete address with id: " + addressId);
        }
        logger.info("Successfully deleted addressId={}", addressId);
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address
     * @return the {@link Address} entity
     * @throws AddressNotFoundException if no address exists
     */
    @Override
    public Address getAddressById(int addressId) {
        logger.debug("Fetching addressId={}", addressId);
        try {
            Address address = addressRepository.findById(addressId);
            logger.info("Found addressId={}", addressId);
            return address;
        } catch (EmptyResultDataAccessException ex) {
            logger.warn("Address not found with id={}", addressId);
            throw new AddressNotFoundException("Address not found with id: " + addressId);
        }
    }

    /**
     * Retrieves all addresses associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Address} entities
     * @throws AddressNotFoundException if no addresses exist
     */
    @Override
    public List<Address> getAddressesByUserId(int userId) {
        logger.debug("Fetching addresses for userId={}", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses == null || addresses.isEmpty()) {
            logger.warn("No addresses found for userId={}", userId);
            throw new AddressNotFoundException("No addresses found for userId: " + userId);
        }
        logger.info("Found {} addresses for userId={}", addresses.size(), userId);
        return addresses;
    }

    /**
     * Retrieves all addresses in the system.
     *
     * @return list of all {@link Address} entities
     * @throws AddressNotFoundException if no addresses exist
     */
    @Override
    public List<Address> getAllAddresses() {
        logger.debug("Fetching all addresses");
        List<Address> addresses = addressRepository.findAll();
        if (addresses == null || addresses.isEmpty()) {
            logger.warn("No addresses found in the system");
            throw new AddressNotFoundException("No addresses found in the system");
        }
        logger.info("Found {} addresses in the system", addresses.size());
        return addresses;
    }
}
