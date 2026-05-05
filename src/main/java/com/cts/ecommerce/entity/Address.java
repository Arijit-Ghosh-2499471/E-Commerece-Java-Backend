package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Address {

    private int addressId;
    private int userId;
    private String houseNo;
    private String area;
    private String city;
    private String state;
    private String country;
    private int pinCode;

    public Address(int userId, String houseNo, String area, String city, String state, String country, int pinCode) {
        this.userId = userId;
        this.houseNo = houseNo;
        this.area = area;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }

}
