package com.sharesphere.service;

import com.sharesphere.dto.response.AdminStatsResponse;
import com.sharesphere.dto.response.ItemResponse;
import com.sharesphere.dto.response.UserResponse;
import java.util.List;

public interface AdminService {
    AdminStatsResponse getStats();
    List<UserResponse> getAllUsers();
    void blockUser(Long userId);
    void unblockUser(Long userId);
    List<ItemResponse> getAllItems();
    void removeItem(Long itemId);
}
