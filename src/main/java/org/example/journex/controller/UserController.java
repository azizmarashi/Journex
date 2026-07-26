package org.example.journex.controller;

import jakarta.validation.Valid;
import org.example.journex.model.ChangePasswordDto;
import org.example.journex.model.UpdateProfileDto;
import org.example.journex.model.UserDto;
import org.example.journex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserDto getCurrentProfile() {
        return userService.getCurrentProfile();
    }

    @PutMapping("/me")
    public UserDto updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        return userService.updateProfile(dto);
    }

    @PostMapping("/me/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
    }

    @GetMapping("/me/subscription/is-active")
    public boolean hasActiveSubscription() {
        return userService.hasActiveSubscription();
    }

}