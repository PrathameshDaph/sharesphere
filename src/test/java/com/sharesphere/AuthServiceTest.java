package com.sharesphere;

import com.sharesphere.dto.request.LoginRequest;
import com.sharesphere.dto.request.RegisterRequest;
import com.sharesphere.dto.response.AuthResponse;
import com.sharesphere.entity.User;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.security.JwtUtil;
import com.sharesphere.service.AuthService;
import com.sharesphere.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtil jwtUtil;
    @Mock AuthenticationManager authManager;
    @InjectMocks AuthServiceImpl authService;

    @Test
    void register_success() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Test User"); req.setEmail("test@college.edu"); req.setPassword("password123");
        when(userRepository.existsByEmail("test@college.edu")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        User saved = User.builder().id(1L).name("Test User").email("test@college.edu").password("hashed").build();
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(jwtUtil.generateToken(any())).thenReturn("jwt-token");

        AuthResponse res = authService.register(req);

        assertNotNull(res);
        assertEquals("jwt-token", res.getToken());
        assertEquals("test@college.edu", res.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("dup@college.edu"); req.setPassword("pass");
        when(userRepository.existsByEmail("dup@college.edu")).thenReturn(true);
        assertThrows(com.sharesphere.exception.ValidationException.class, () -> authService.register(req));
    }

    @Test
    void login_success() {
        LoginRequest req = new LoginRequest();
        req.setEmail("rahul@college.edu"); req.setPassword("Student@123");
        User user = User.builder().id(1L).name("Rahul").email("rahul@college.edu").password("hashed").build();
        when(userRepository.findByEmail("rahul@college.edu")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("login-token");

        AuthResponse res = authService.login(req);
        assertEquals("login-token", res.getToken());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
