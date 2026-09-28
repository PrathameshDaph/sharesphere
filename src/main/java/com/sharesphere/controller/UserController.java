package com.sharesphere.controller;

import com.sharesphere.dto.request.UpdateProfileRequest;
import com.sharesphere.dto.response.UserResponse;
import com.sharesphere.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(userService.getCurrentUser(ud.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal UserDetails ud,
            @Valid @RequestBody UpdateProfileRequest req) {
        com.sharesphere.entity.User user = (com.sharesphere.entity.User) ud;
        return ResponseEntity.ok(userService.updateProfile(user.getId(), req));
    }
}
