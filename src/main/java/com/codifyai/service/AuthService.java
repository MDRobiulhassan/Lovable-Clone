package com.codifyai.service;

import com.codifyai.dto.auth.AuthResponse;
import com.codifyai.dto.auth.LoginRequest;
import com.codifyai.dto.auth.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
