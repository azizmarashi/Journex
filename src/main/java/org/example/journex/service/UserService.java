package org.example.journex.service;

import org.example.journex.configs.exception.JournexException;
import org.example.journex.dao.UserRepository;
import org.example.journex.domain.User;
import org.example.journex.enums.SubscriptionPlan;
import org.example.journex.enums.SubscriptionStatus;
import org.example.journex.enums.UserRole;
import org.example.journex.mapper.UserMapper;
import org.example.journex.model.ChangePasswordDto;
import org.example.journex.model.Pagination;
import org.example.journex.model.UserDto;
import org.example.journex.model.UpdateProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Transactional
    public UserDto updateSubscription(SubscriptionPlan plan, SubscriptionStatus status, LocalDateTime expireAt) {
        User user = authService.getCurrentUser();
        user.setSubscriptionPlan(plan);
        user.setSubscriptionStatus(status);
        user.setSubscriptionExpireAt(expireAt);
        user.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveSubscription() {
        User user = authService.getCurrentUser();
        if (user.getSubscriptionStatus() != SubscriptionStatus.ACTIVE) return false;
        return user.getSubscriptionExpireAt() == null
                || user.getSubscriptionExpireAt().isAfter(LocalDateTime.now());
    }

    @Transactional
    public UserDto updateRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JournexException("error.user.notFound"));
        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void setEnabled(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new JournexException("error.user.notFound"));
        user.setEnabled(enabled);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

}
