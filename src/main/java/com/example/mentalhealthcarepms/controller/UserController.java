package com.example.mentalhealthcarepms.controller;

import com.example.mentalhealthcarepms.config.JwtService;
import com.example.mentalhealthcarepms.dto.ResponseObject;
import com.example.mentalhealthcarepms.dto.UserLoginRequest;
import com.example.mentalhealthcarepms.dto.UserLoginResponse;
import com.example.mentalhealthcarepms.dto.UsersResponse;
import com.example.mentalhealthcarepms.entity.User;
import com.example.mentalhealthcarepms.exception.UserNotFoundException;
import com.example.mentalhealthcarepms.service.UserService;
import com.example.mentalhealthcarepms.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping("/roles")
    public ResponseEntity<?> getAllUserRoles() {

        ResponseObject responseObject = new ResponseObject();

        List<String> urs = new ArrayList<>();

        for(Constants.UserRole ur : Constants.UserRole.values()) {
            urs.add(ur.value());
        }

        if(!urs.isEmpty()) {
            responseObject.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            responseObject.setResponseMessage("Get all user role successfully");
            return new ResponseEntity(responseObject, HttpStatus.OK);
        }
        else {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("gender")
    public ResponseEntity<?> getAllUserGenders() {

        ResponseObject response = new ResponseObject();
        List<String> genders = new ArrayList<>();

        for(Constants.Sex gender : Constants.Sex.values() ) {
            genders.add(gender.value());
        }

        if(genders.isEmpty()) {
            response.setResponseCode(Constants.ResponseCode.FAIL.value());
            response.setResponseMessage("Failed");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        else {
            response.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            response.setResponseMessage("Get all genders successfully");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        ResponseObject responseObject = new ResponseObject();

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setStatus(Constants.UserStatus.ACTIVE.value());

        User registerUser = userService.registerUser(user);

        if(registerUser != null) {
            responseObject.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            responseObject.setResponseMessage("Resgister successfully");
            return new ResponseEntity(responseObject, HttpStatus.OK);
        }
        else {
            responseObject.setResponseCode(Constants.ResponseCode.FAIL.value());
            responseObject.setResponseMessage("Failed");
            return new ResponseEntity(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        String jwt = null;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        }
        catch(Exception ex) {
            userLoginResponse.setResponseCode(Constants.ResponseCode.FAIL.value());
            userLoginResponse.setResponseMessage("Failed to login as" + userLoginRequest.getEmail());
            return new ResponseEntity(userLoginResponse, HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginRequest.getEmail());
        User user = userService.getUserByEmail(userLoginRequest.getEmail());

        if(user.getStatus() != Constants.UserStatus.ACTIVE.value()) {
            userLoginResponse.setResponseCode(Constants.ResponseCode.FAIL.value());
            userLoginResponse.setResponseMessage("Failed");
            return new ResponseEntity(userLoginResponse, HttpStatus.BAD_REQUEST);
        }

        for(GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            if(grantedAuthority.getAuthority().equals(userDetails.getAuthorities())) {
                jwt =jwtService.generateToken(userDetails.getUsername());
            }
        }

        if(jwt != null) {
            userLoginResponse =User.toUserLoginResponse(user);
            userLoginResponse.setResponseCode(Constants.ResponseCode.SUCCESS.value());
            userLoginResponse.setResponseMessage("Login successfully");
            userLoginResponse.setJwtToken(jwt);
            return new ResponseEntity(userLoginResponse, HttpStatus.OK);
        }
        else {
            userLoginResponse.setResponseCode(Constants.ResponseCode.FAIL.value());
            userLoginResponse.setResponseMessage("Failed");
            return new ResponseEntity(userLoginResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/id")
    public ResponseEntity<?> getUserById(@RequestParam("userId") int userId) {

        UsersResponse usersResponse = new UsersResponse();

        User user = userService.getUserById(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }

        usersResponse.setUser(user);
        usersResponse.setResponseCode(Constants.ResponseCode.SUCCESS.value());
        usersResponse.setResponseMessage("Successfully");
        return new ResponseEntity(usersResponse, HttpStatus.OK);
    }


    @GetMapping("delete/id")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") int userId) {

        ResponseObject response = new ResponseObject();

        User user = userService.getUserById(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }

        user.setStatus(Constants.UserStatus.DELETED.value());

        userService.registerUser(user);

        response.setResponseCode(Constants.ResponseCode.SUCCESS.value());
        response.setResponseMessage("Successfully");

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
