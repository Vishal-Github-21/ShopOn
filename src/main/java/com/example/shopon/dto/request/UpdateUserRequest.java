package com.example.shopon.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String fullName;
    private String phoneNumber;

}