package com.example.mentalhealthcarepms.controller;

import com.example.mentalhealthcarepms.dto.DoctorRegister;
import com.example.mentalhealthcarepms.dto.ResponseObject;
import com.example.mentalhealthcarepms.entity.User;
import com.example.mentalhealthcarepms.service.UserService;
import com.example.mentalhealthcarepms.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("?register")
    public ResponseEntity<?> registerDoctor(@RequestBody DoctorRegister doctorRegister) {

        ResponseObject responseObject = new ResponseObject();

        User user = DoctorRegister.toEntity(doctorRegister);

        String password = passwordEncoder.encode(doctorRegister.getPassword());
        user.setPassword(password);
        user.setStatus(Constants.UserStatus.ACTIVE.value());

        User registerUser = userService.registerUser(user);

        if(registerUser != null) {
            responseObject.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            responseObject.setResponseMessage("register doctor successfully");
            return new ResponseEntity(responseObject, HttpStatus.OK);
        }
        else {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllDoctors() {

        List<User> doctors = userService.getAllUserByRole(Constants.UserRole.DOCTOR.value());

        return new ResponseEntity(doctors, HttpStatus.OK);
    }


    @GetMapping("/specialist/all")
    public ResponseEntity<?> getAllSpecialists() {

        List<String> specialists = new ArrayList<>();

        for(Constants.DoctorSpecialist sp : Constants.DoctorSpecialist.values()) {
            specialists.add(sp.value());
        }

        return new ResponseEntity(specialists, HttpStatus.OK);
    }

}
