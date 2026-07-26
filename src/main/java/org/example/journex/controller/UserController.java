package org.example.journex.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.journex.model.ChangePasswordDto;
import org.example.journex.model.UpdateProfileDto;
import org.example.journex.model.UserDto;
import org.example.journex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserDto getCurrentProfile() {
        return userService.getCurrentProfile();
    }

    @PutMapping("/update")
    public UserDto updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        return userService.updateProfile(dto);
    }

    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
    }

}