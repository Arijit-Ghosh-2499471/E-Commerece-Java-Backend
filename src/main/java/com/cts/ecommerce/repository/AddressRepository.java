package com.cts.ecommerce.repository;

import com.cts.ecommerce.entity.Address;
import java.util.List;

public interface AddressRepository {
    int save(Address address);
    int update(Address address);
    int delete(int addressId);
    Address findById(int addressId);
    List<Address> findByUserId(int userId);
    List<Address> findAll();
    int getIdOfAddress(int userId);
}
