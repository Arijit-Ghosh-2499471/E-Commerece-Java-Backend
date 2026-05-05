package com.cts.ecommerce.repository;

import java.util.List;
import com.cts.ecommerce.entity.User;

public interface UserRepository {

    int save(User user);

    List<User> findAll();

    User findById(int id);

    int delete(int id);

    String getPassword(String email);

    User findByEmail(String email);

    int update(int id, User existingUser);
}

