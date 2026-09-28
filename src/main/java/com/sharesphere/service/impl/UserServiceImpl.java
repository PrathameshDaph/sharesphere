package com.sharesphere.service.impl;

import com.sharesphere.dto.request.UpdateProfileRequest;
import com.sharesphere.dto.response.UserResponse;
import com.sharesphere.entity.User;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(Long userId) {
        return toResponse(findUser(userId));
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(user);
    }

    @Override
    public UserResponse updateProfile(Long userId, UpdateProfileRequest req) {
        User user = findUser(userId);
        if (req.getName() != null) user.setName(req.getName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getCollege() != null) user.setCollege(req.getCollege());
        if (req.getLocation() != null) user.setLocation(req.getLocation());
        if (req.getBio() != null) user.setBio(req.getBio());
        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateProfileImage(Long userId, String imageUrl) {
        User user = findUser(userId);
        user.setProfileImage(imageUrl);
        return toResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void blockUser(Long userId) {
        User user = findUser(userId);
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = findUser(userId);
        user.setBlocked(false);
        userRepository.save(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId()).name(u.getName()).email(u.getEmail())
                .phone(u.getPhone()).college(u.getCollege()).location(u.getLocation())
                .bio(u.getBio()).profileImage(u.getProfileImage()).role(u.getRole())
                .blocked(u.isBlocked()).averageRating(u.getAverageRating())
                .totalRatings(u.getTotalRatings()).createdAt(u.getCreatedAt())
                .build();
    }
}
