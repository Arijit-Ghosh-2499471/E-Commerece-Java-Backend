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
    public int addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public int getIdOfAddress(int userId) {
        return addressRepository.getIdOfAddress(userId);
    }

    @Override
    public int updateAddress(Address address) {
        return addressRepository.update(address);
    }

    @Override
    public int deleteAddress(int addressId) {
        return addressRepository.delete(addressId);
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
