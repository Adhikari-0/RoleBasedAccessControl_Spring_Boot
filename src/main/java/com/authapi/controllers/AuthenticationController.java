package com.authapi.controllers;

import com.authapi.dtos.LoginUserDto;
import com.authapi.dtos.RegisterUserDto;
import com.authapi.entities.User;
import com.authapi.responses.LoginResponse;
import com.authapi.services.AuthenticationService;
import com.authapi.services.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    	System.out.println("Class: AuthenticationController, Method: ResponseEntity(auth/signup)");
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    	System.out.println("loginUserDto: "+loginUserDto);
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        System.out.println("authenticatedUser: "+authenticatedUser);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        		
        return ResponseEntity.ok(loginResponse);
    }
}
