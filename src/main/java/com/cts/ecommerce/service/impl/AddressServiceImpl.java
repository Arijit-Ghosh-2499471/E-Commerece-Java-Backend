package com.cts.ecommerce.service.impl;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.repository.AddressRepository;
import com.cts.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void addAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public int getIdOfAddress(int userId) {
        return addressRepository.getIdOfRecentAddress(userId);
    }

    @Override
    public int updateAddress(Address address) {
        return addressRepository.update(address);
    }

    @Override
    public void deleteAddress(int addressId) {
        addressRepository.delete(addressId);
    }

    @Override
    public Address getAddressById(int addressId) {
        return addressRepository.findById(addressId);
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }
}
