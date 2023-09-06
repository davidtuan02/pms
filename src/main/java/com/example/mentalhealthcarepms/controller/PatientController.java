package com.example.mentalhealthcarepms.controller;

import com.example.mentalhealthcarepms.entity.User;
import com.example.mentalhealthcarepms.service.UserService;
import com.example.mentalhealthcarepms.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private UserService userService;

    @GetMapping("/bloodgroup/all")
    public ResponseEntity<?> getAllBloodGroup() {

        List<String> bloodGroups = new ArrayList<>();

        for(Constants.BloodGroup bg : Constants.BloodGroup.values()) {
            bloodGroups.add(bg.value());
        }

        return new ResponseEntity(bloodGroups, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllPatient() {

        List<User> patients = userService.getAllUserByRole(Constants.UserRole.PATIENT.value());

        return new ResponseEntity(patients, HttpStatus.OK);

    }
}
