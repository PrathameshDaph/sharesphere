package com.sharesphere.dto.request;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String phone;
    private String college;
    private String location;
    private String bio;
}
