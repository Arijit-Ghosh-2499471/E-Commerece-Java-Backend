package com.cts.ecommerce.entity;

public class Users {

    private int UserId;
    private String Name;
    private String Email;
    private String Password;
    private String PaymentDetails;
    private String Role;

    public int getUserId() { return UserId; }
    public void setUserId(int userId) { UserId = userId; }

    public String getName() { return Name; }
    public void setName(String name) { Name = name; }

    public String getEmail() { return Email; }
    public void setEmail(String email) { Email = email; }

    public String getPassword() { return Password; }
    public void setPassword(String password) { Password = password; }

    public String getPaymentDetails() { return PaymentDetails; }
    public void setPaymentDetails(String paymentDetails) { PaymentDetails = paymentDetails; }

    public String getRole() { return Role; }
    public void setRole(String role) { Role = role; }
}
