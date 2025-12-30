package com.example.shopon.service;

import com.example.shopon.dto.request.CreateUserRequest;
import com.example.shopon.dto.response.UserResponse;
import com.example.shopon.dto.request.UpdateUserRequest;
import com.example.shopon.entity.User;
import com.example.shopon.exception.UserNotFoundException;
import com.example.shopon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.Builder.user()
                .withFullName(request.getFullName())
                .withEmail(request.getEmail())
                .withPhoneNumber(request.getPhoneNumber())
                .withPassword(request.getPassword())
                .build();
        return mapToResponse(userRepository.save(user));
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }

    public void sendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        user.setOtp(otp);
        userRepository.save(user);

        System.out.println("OTP for " + email + " is: " + otp);
    }

    public void verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setEmailVerified(true);
        user.setOtp(null);

        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
