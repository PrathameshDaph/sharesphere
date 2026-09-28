package com.sharesphere.service;

import com.sharesphere.dto.request.LoginRequest;
import com.sharesphere.dto.request.RegisterRequest;
import com.sharesphere.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
