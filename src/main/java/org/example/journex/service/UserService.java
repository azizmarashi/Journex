package org.example.journex.service;

import org.example.journex.config.exception.JournexException;
import org.example.journex.dao.UserRepository;
import org.example.journex.domain.User;
import org.example.journex.mapper.UserMapper;
import org.example.journex.model.ChangePasswordDto;
import org.example.journex.model.UserDto;
import org.example.journex.model.UpdateProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserDto getCurrentProfile() {
        User user = authService.getCurrentUser();
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateProfile(UpdateProfileDto dto) {
        User user = authService.getCurrentUser();
        if (dto.getNickname() != null)
            user.setNickname(dto.getNickname());
        if (dto.getProfileImageUrl() != null)
            user.setProfileImageUrl(dto.getProfileImageUrl());
        if (dto.getPhoneNumber() != null)
            user.setPhoneNumber(dto.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void changePassword(ChangePasswordDto dto) {
        User user = authService.getCurrentUser();
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword()))
            throw new JournexException("error.password.incorrect");
        if (dto.getOldPassword().equals(dto.getNewPassword()))
            throw new JournexException("error.password.sameAsOld");
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

}
