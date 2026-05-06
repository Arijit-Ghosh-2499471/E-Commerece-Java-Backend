package com.cts.ecommerce.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Entity representing the Users table in the ecommerce schema.
 * Maps to:
 *   Users(UserId INT PK, Name VARCHAR, Email VARCHAR,
 *      Password VARCHAR, PaymentDetails VARCHAR, Role VARCHAR)
 */

@Getter
@Setter
@NoArgsConstructor
@Component
public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String paymentDetails;
    private String role;

    public User(String name, String email, String password, String paymentDetails, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.paymentDetails = paymentDetails;
        this.role = role;
    }
}
