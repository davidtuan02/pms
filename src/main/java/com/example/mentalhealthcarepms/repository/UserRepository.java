package com.example.mentalhealthcarepms.repository;

import com.example.mentalhealthcarepms.entity.Appointment;
import com.example.mentalhealthcarepms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
    User findByEmailAndPasswordAndRole(String email, String password, String role);
    User findByEmailAndRole(String email, String role);
    User findByEmail(String email);
    List<User> findByRoleAndStatus(String role, int status);
}
