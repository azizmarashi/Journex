package org.example.journex.controller;

import jakarta.validation.Valid;
import org.example.journex.model.LoginRequest;
import org.example.journex.model.RegisterRequest;
import org.example.journex.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Long register( @RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {
           return authService.logout(authHeader);
    }

}