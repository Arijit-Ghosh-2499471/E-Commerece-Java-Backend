package com.cts.ecommerce.service;


import java.util.List;
import com.cts.ecommerce.entity.Users;

public interface UsersService {

    int createUser(Users user);

    List<Users> getAllUsers();

    Users getUserById(int id);

    int deleteUser(int id);
}

