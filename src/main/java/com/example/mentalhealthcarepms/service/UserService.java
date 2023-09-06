package com.example.mentalhealthcarepms.service;

import com.example.mentalhealthcarepms.entity.User;

import java.util.List;

public interface UserService {
    public User registerUser(User user);
    public User getUserByEmailAndPassword(String email, String password);
    public User getUserByEmailAndPasswordAndRole(String email, String password, String role);
    public User getUserByEmailAndRole(String email, String role);
    public User getUserByEmail(String email);
    public User getUserById(int id);

    public User updateUser(User user);

    public List<User> getAllUserByRole(String role);
    public void deleteUser(User user);
}
