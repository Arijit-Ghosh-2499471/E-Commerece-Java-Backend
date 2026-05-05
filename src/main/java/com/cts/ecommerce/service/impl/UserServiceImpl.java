package com.cts.ecommerce.service.impl;

import java.util.List;

import com.cts.ecommerce.repository.ShoppingCartRepository;
import com.cts.ecommerce.service.UserService;
import org.springframework.stereotype.Service;

import com.cts.ecommerce.entity.User;
import com.cts.ecommerce.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public UserServiceImpl(UserRepository userRepository, ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public int createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public int deleteUser(int id) {
        return userRepository.delete(id);
    }

    @Override
    public boolean login(String email, String password) {
        return userRepository.getPassword(email).equals(password);
    }

    @Override
    public void register(User user) {
//        userRepository.save(user);
//        shoppingCartRepository.createCart(user.getUserId());

        userRepository.save(user);              // INSERT happens
        User savedUser = userRepository.findByEmail(user.getEmail()); // FETCH id
        shoppingCartRepository.createCart(savedUser.getUserId());

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public int updateUser(int id, User user) {
        User existingUser = userRepository.findById(id);

        if (existingUser == null) {
            return 0; // or throw exception
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setPaymentDetails(user.getPaymentDetails());
        existingUser.setRole(user.getRole());

        return userRepository.update(id, existingUser);
    }
}