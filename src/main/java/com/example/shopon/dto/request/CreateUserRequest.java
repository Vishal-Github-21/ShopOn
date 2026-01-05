package com.example.shopon.dto.request;

import com.example.shopon.entity.Role;
import lombok.*;

@Data
public class CreateUserRequest {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;

}
