package com.cts.ecommerce.repository;

import java.util.List;
import com.cts.ecommerce.entity.Users;

public interface UsersRepository {

    int save(Users user);

    List<Users> findAll();

    Users findById(int id);

    int delete(int id);
}

