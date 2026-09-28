package com.sharesphere.service.impl;

import com.sharesphere.dto.request.LoginRequest;
import com.sharesphere.dto.request.RegisterRequest;
import com.sharesphere.dto.response.AuthResponse;
import com.sharesphere.entity.User;
import com.sharesphere.exception.ValidationException;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.security.JwtUtil;
import com.sharesphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .college(request.getCollege())
                .location(request.getLocation())
                .build();
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user);
        log.info("New user registered: {}", user.getEmail());
        return AuthResponse.builder()
                .token(token).userId(user.getId())
                .name(user.getName()).email(user.getEmail()).role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(token).userId(user.getId())
                .name(user.getName()).email(user.getEmail()).role(user.getRole())
                .build();
    }
}
