package com.lovable.lovable_clone.service.impl;

import com.lovable.lovable_clone.dto.auth.AuthResponse;
import com.lovable.lovable_clone.dto.auth.LoginRequest;
import com.lovable.lovable_clone.dto.auth.SignupRequest;
import com.lovable.lovable_clone.entity.User;
import com.lovable.lovable_clone.error.BadRequestException;
import com.lovable.lovable_clone.mapper.UserMapper;
import com.lovable.lovable_clone.repository.UserRepository;
import com.lovable.lovable_clone.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("User Already Exists with username: " + request.username());
        });

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return new AuthResponse("dummy-token", userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
