package com.cts.ecommerce.service;


import java.util.List;
import com.cts.ecommerce.entity.User;

public interface UsersService {

    int createUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    int deleteUser(int id);
}

