package com.cts.ecommerce.service.impl;

import java.util.List;

import com.cts.ecommerce.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ecommerce.entity.Users;
import com.cts.ecommerce.repository.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public int createUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users getUserById(int id) {
        return usersRepository.findById(id);
    }

    @Override
    public int deleteUser(int id) {
        return usersRepository.delete(id);
    }
}