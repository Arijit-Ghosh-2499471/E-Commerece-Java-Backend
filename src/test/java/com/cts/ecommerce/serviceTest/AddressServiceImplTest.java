package com.cts.ecommerce.serviceTest;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.exception.AddressCreationException;
import com.cts.ecommerce.exception.AddressDeletionException;
import com.cts.ecommerce.exception.AddressNotFoundException;
import com.cts.ecommerce.exception.AddressUpdateException;
import com.cts.ecommerce.repository.AddressRepository;
import com.cts.ecommerce.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AddressServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;

    /**
     * Creates common test data before each test.
     */
    @BeforeEach
    void setUp() {
        address = new Address();
        address.setAddressId(1);
        address.setUserId(10);
        address.setHouseNo("12A");
        address.setArea("Anna Nagar");
        address.setCity("Chennai");
        address.setState("Tamil Nadu");
        address.setCountry("India");
        address.setPinCode(600040);
    }

    /**
     * Tests successful address creation.
     */
    @Test
    void addAddress_ShouldSaveAddress_WhenAddressIsCreatedSuccessfully() {
        when(addressRepository.save(address)).thenReturn(1);

        assertDoesNotThrow(() -> addressService.addAddress(address));

        verify(addressRepository, times(1)).save(address);
    }

    /**
     * Tests AddressCreationException when address creation returns zero rows.
     */
    @Test
    void addAddress_ShouldThrowAddressCreationException_WhenSaveReturnsZero() {
        when(addressRepository.save(address)).thenReturn(0);

        AddressCreationException exception = assertThrows(
                AddressCreationException.class,
                () -> addressService.addAddress(address)
        );

        assertTrue(exception.getMessage().contains("Failed to insert address"));
        verify(addressRepository, times(1)).save(address);
    }

    /**
     * Tests successful retrieval of recent address ID.
     */
    @Test
    void getIdOfAddress_ShouldReturnAddressId_WhenRecentAddressExists() {
        when(addressRepository.getIdOfRecentAddress(10)).thenReturn(1);

        int result = addressService.getIdOfAddress(10);

        assertEquals(1, result);
        verify(addressRepository, times(1)).getIdOfRecentAddress(10);
    }

    /**
     * Tests AddressNotFoundException when recent address ID is not found.
     */
    @Test
    void getIdOfAddress_ShouldThrowAddressNotFoundException_WhenRecentAddressDoesNotExist() {
        when(addressRepository.getIdOfRecentAddress(10)).thenReturn(-1);

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class,
                () -> addressService.getIdOfAddress(10)
        );

        assertTrue(exception.getMessage().contains("No recent address found"));
        verify(addressRepository, times(1)).getIdOfRecentAddress(10);
    }

    /**
     * Tests successful address update.
     */
    @Test
    void updateAddress_ShouldReturnOne_WhenAddressIsUpdatedSuccessfully() {
        when(addressRepository.update(address)).thenReturn(1);

        int result = addressService.updateAddress(address);

        assertEquals(1, result);
        verify(addressRepository, times(1)).update(address);
    }

    /**
     * Tests AddressUpdateException when update returns zero rows.
     */
    @Test
    void updateAddress_ShouldThrowAddressUpdateException_WhenUpdateReturnsZero() {
        when(addressRepository.update(address)).thenReturn(0);

        AddressUpdateException exception = assertThrows(
                AddressUpdateException.class,
                () -> addressService.updateAddress(address)
        );

        assertTrue(exception.getMessage().contains("Failed to update address"));
        verify(addressRepository, times(1)).update(address);
    }

    /**
     * Tests successful address deletion.
     */
    @Test
    void deleteAddress_ShouldDeleteAddress_WhenAddressIsDeletedSuccessfully() {
        when(addressRepository.delete(1)).thenReturn(1);

        assertDoesNotThrow(() -> addressService.deleteAddress(1));

        verify(addressRepository, times(1)).delete(1);
    }

    /**
     * Tests AddressDeletionException when delete returns zero rows.
     */
    @Test
    void deleteAddress_ShouldThrowAddressDeletionException_WhenDeleteReturnsZero() {
        when(addressRepository.delete(1)).thenReturn(0);

        AddressDeletionException exception = assertThrows(
                AddressDeletionException.class,
                () -> addressService.deleteAddress(1)
        );

        assertTrue(exception.getMessage().contains("Failed to delete address"));
        verify(addressRepository, times(1)).delete(1);
    }

    /**
     * Tests fetching address by ID successfully.
     */
    @Test
    void getAddressById_ShouldReturnAddress_WhenAddressExists() {
        when(addressRepository.findById(1)).thenReturn(address);

        Address result = addressService.getAddressById(1);

        assertNotNull(result);
        assertEquals(1, result.getAddressId());
        assertEquals("Chennai", result.getCity());
        verify(addressRepository, times(1)).findById(1);
    }

    /**
     * Tests AddressNotFoundException when repository throws EmptyResultDataAccessException.
     */
    @Test
    void getAddressById_ShouldThrowAddressNotFoundException_WhenAddressDoesNotExist() {
        when(addressRepository.findById(1))
                .thenThrow(new EmptyResultDataAccessException(1));

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class,
                () -> addressService.getAddressById(1)
        );

        assertTrue(exception.getMessage().contains("Address not found with id"));
        verify(addressRepository, times(1)).findById(1);
    }

    /**
     * Tests fetching addresses by user ID successfully.
     */
    @Test
    void getAddressesByUserId_ShouldReturnAddressList_WhenAddressesExist() {
        when(addressRepository.findByUserId(10)).thenReturn(List.of(address));

        List<Address> addresses = addressService.getAddressesByUserId(10);

        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        assertEquals(10, addresses.getFirst().getUserId());
        verify(addressRepository, times(1)).findByUserId(10);
    }

    /**
     * Tests AddressNotFoundException when addresses by user ID returns empty list.
     */
    @Test
    void getAddressesByUserId_ShouldThrowAddressNotFoundException_WhenAddressListIsEmpty() {
        when(addressRepository.findByUserId(10)).thenReturn(List.of());

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class,
                () -> addressService.getAddressesByUserId(10)
        );

        assertTrue(exception.getMessage().contains("No addresses found for userId"));
        verify(addressRepository, times(1)).findByUserId(10);
    }

    /**
     * Tests AddressNotFoundException when addresses by user ID returns null.
     */
    @Test
    void getAddressesByUserId_ShouldThrowAddressNotFoundException_WhenAddressListIsNull() {
        when(addressRepository.findByUserId(10)).thenReturn(null);

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class,
                () -> addressService.getAddressesByUserId(10)
        );

        assertTrue(exception.getMessage().contains("No addresses found for userId"));
        verify(addressRepository, times(1)).findByUserId(10);
    }

    /**
     * Tests fetching all addresses successfully.
     */
    @Test
    void getAllAddresses_ShouldReturnAddressList_WhenAddressesExist() {
        when(addressRepository.findAll()).thenReturn(List.of(address));

        List<Address> addresses = addressService.getAllAddresses();

        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        assertEquals("Anna Nagar", addresses.getFirst().getArea());
        verify(addressRepository, times(1)).findAll();
    }

    /**
     * Tests AddressNotFoundException when all addresses list is empty.
     */
    @Test
    void getAllAddresses_ShouldThrowAddressNotFoundException_WhenAddressListIsEmpty() {
        when(addressRepository.findAll()).thenReturn(List.of());

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class,
                () -> addressService.getAllAddresses()
        );

        assertTrue(exception.getMessage().contains("No addresses found in the system"));
        verify(addressRepository, times(1)).findAll();
    }

    /**
     * Tests AddressNotFoundException when all addresses list is null.
     */
    @Test
    void getAllAddresses_ShouldThrowAddressNotFoundException_WhenAddressListIsNull() {
        when(addressRepository.findAll()).thenReturn(null);

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class,
                () -> addressService.getAllAddresses()
        );

        assertTrue(exception.getMessage().contains("No addresses found in the system"));
        verify(addressRepository, times(1)).findAll();
    }
}