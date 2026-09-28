package com.sharesphere.dto.response;
import com.sharesphere.entity.Role;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String college;
    private String location;
    private String bio;
    private String profileImage;
    private Role role;
    private boolean blocked;
    private double averageRating;
    private int totalRatings;
    private LocalDateTime createdAt;
}
