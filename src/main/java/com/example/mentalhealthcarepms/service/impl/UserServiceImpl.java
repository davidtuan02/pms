package com.example.mentalhealthcarepms.service.impl;

import com.example.mentalhealthcarepms.entity.User;
import com.example.mentalhealthcarepms.repository.UserRepository;
import com.example.mentalhealthcarepms.service.UserService;
import com.example.mentalhealthcarepms.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        User registerUser = null;
        if(user != null) {
            registerUser = userRepository.save(user);
        }
        return registerUser;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User getUserByEmailAndPasswordAndRole(String email, String password, String role) {
        return userRepository.findByEmailAndPasswordAndRole(email, password, role);
    }

    @Override
    public User getUserByEmailAndRole(String email, String role) {
        return userRepository.findByEmailAndRole(email, role);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUserByRole(String role) {
        return userRepository.findByRoleAndStatus(role, Constants.UserStatus.ACTIVE.value());
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);

    }
}
