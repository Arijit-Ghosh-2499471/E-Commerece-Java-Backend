package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Address;
import java.util.List;

/**
 * Service interface for managing {@link Address} entities.
 * Provides CRUD operations and query methods for user addresses.
 */
public interface AddressService {

    /**
     * Adds a new address to the system.
     *
     * @param address the {@link Address} entity to be saved
     */
    void addAddress(Address address);

    /**
     * Retrieves the ID of the most recent address for a given user.
     *
     * @param userId the ID of the user
     * @return the ID of the most recent address, or -1 if none exists
     */
    int getIdOfAddress(int userId);

    /**
     * Updates an existing address in the system.
     *
     * @param address the {@link Address} entity with updated details
     * @return number of rows affected (0 if update failed)
     */
    int updateAddress(Address address);

    /**
     * Deletes an address by its ID.
     *
     * @param addressId the ID of the address to delete
     */
    void deleteAddress(int addressId);

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address
     * @return the {@link Address} entity if found
     */
    Address getAddressById(int addressId);

    /**
     * Retrieves all addresses associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Address} entities for the user
     */
    List<Address> getAddressesByUserId(int userId);

    /**
     * Retrieves all addresses in the system.
     *
     * @return list of all {@link Address} entities
     */
    List<Address> getAllAddresses();
}
