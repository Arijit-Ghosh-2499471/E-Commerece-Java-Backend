package com.cts.ecommerce.service;

import com.cts.ecommerce.entity.Address;
import java.util.List;

public interface AddressService {
    int addAddress(Address address);
    int getIdOfLatestAddress(int userId);
    int updateAddress(Address address);
    int deleteAddress(int addressId);
    Address getAddressById(int addressId);
    List<Address> getAddressesByUserId(int userId);
    List<Address> getAllAddresses();
}
