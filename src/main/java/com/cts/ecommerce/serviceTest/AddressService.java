package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Address;
import java.util.List;

public interface AddressService {
    void addAddress(Address address);
    int getIdOfAddress(int userId);
    int updateAddress(Address address);
    void deleteAddress(int addressId);
    Address getAddressById(int addressId);
    List<Address> getAddressesByUserId(int userId);
    List<Address> getAllAddresses();
}
