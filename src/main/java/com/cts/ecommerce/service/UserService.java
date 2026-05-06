package com.cts.ecommerce.service;

import java.util.List;
import com.cts.ecommerce.entity.User;

public interface UserService {

    int createUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    int deleteUser(int id);

    boolean login(String email, String password);

    void register(User user);

    User getUserByEmail(String email);

    int updateUser(int id, User user);
}

