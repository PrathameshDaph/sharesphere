package com.sharesphere.service;

import com.sharesphere.dto.request.UpdateProfileRequest;
import com.sharesphere.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse getProfile(Long userId);
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);
    UserResponse updateProfileImage(Long userId, String imageUrl);
    List<UserResponse> getAllUsers();
    void blockUser(Long userId);
    void unblockUser(Long userId);
    UserResponse getCurrentUser(String email);
}
