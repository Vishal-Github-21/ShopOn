package com.example.shopon.dto.response;

import com.example.shopon.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean emailVerified;
    private Role role;

}