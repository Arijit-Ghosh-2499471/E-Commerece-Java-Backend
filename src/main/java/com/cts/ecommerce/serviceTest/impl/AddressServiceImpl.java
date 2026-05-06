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

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void addAddress(Address address) {
        int rows = addressRepository.save(address);
        if (rows == 0) {
            throw new AddressCreationException("Failed to insert address for userId: " + address.getUserId());
        }
    }

    @Override
    public int getIdOfAddress(int userId) {
        int id = addressRepository.getIdOfRecentAddress(userId);
        if (id == -1) {
            throw new AddressNotFoundException("No recent address found for userId: " + userId);
        }
        return id;
    }

    @Override
    public int updateAddress(Address address) {
        int rows = addressRepository.update(address);
        if (rows == 0) {
            throw new AddressUpdateException("Failed to update address with id: " + address.getAddressId());
        }
        return rows;
    }

    @Override
    public void deleteAddress(int addressId) {
        int rows = addressRepository.delete(addressId);
        if (rows == 0) {
            throw new AddressDeletionException("Failed to delete address with id: " + addressId);
        }
    }

    @Override
    public Address getAddressById(int addressId) {
        try {
            return addressRepository.findById(addressId);
        } catch (EmptyResultDataAccessException ex) {
            throw new AddressNotFoundException("Address not found with id: " + addressId);
        }
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses == null || addresses.isEmpty()) {
            throw new AddressNotFoundException("No addresses found for userId: " + userId);
        }
        return addresses;
    }

    @Override
    public List<Address> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        if (addresses == null || addresses.isEmpty()) {
            throw new AddressNotFoundException("No addresses found in the system");
        }
        return addresses;
    }
}
