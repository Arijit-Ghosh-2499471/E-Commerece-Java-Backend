package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.Address;
import java.util.List;

/**
 * Repository interface for managing {@link Address} entities.
 * Provides CRUD operations and query methods for user addresses.
 */
public interface AddressRepository {

    /**
     * Saves a new address to the database.
     *
     * @param address the {@link Address} entity to be saved
     * @return number of rows affected (0 if insert failed)
     */
    int save(Address address);

    /**
     * Updates an existing address in the database.
     *
     * @param address the {@link Address} entity with updated details
     * @return number of rows affected (0 if update failed)
     */
    int update(Address address);

    /**
     * Deletes an address by its ID.
     *
     * @param addressId the ID of the address to delete
     * @return number of rows affected (0 if delete failed)
     */
    int delete(int addressId);

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address
     * @return the {@link Address} entity if found
     */
    Address findById(int addressId);

    /**
     * Retrieves all addresses associated with a given user.
     *
     * @param userId the ID of the user
     * @return list of {@link Address} entities for the user
     */
    List<Address> findByUserId(int userId);

    /**
     * Retrieves all addresses in the system.
     *
     * @return list of all {@link Address} entities
     */
    List<Address> findAll();

    /**
     * Retrieves the ID of the most recent address for a given user.
     *
     * @param userId the ID of the user
     * @return the ID of the most recent address, or -1 if none exists
     */
    int getIdOfRecentAddress(int userId);
}
