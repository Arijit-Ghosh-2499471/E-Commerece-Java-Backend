package com.cts.ecommerce.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Users {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String paymentDetails;
    private String role;

    public Users(String name, String email, String password, String paymentDetails, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.paymentDetails = paymentDetails;
        this.role = role;
    }
}
