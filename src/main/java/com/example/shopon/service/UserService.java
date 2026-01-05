package com.example.shopon.service;

import com.example.shopon.dto.request.CreateUserRequest;
import com.example.shopon.dto.response.UserResponse;
import com.example.shopon.dto.request.UpdateUserRequest;
import com.example.shopon.entity.Role;
import com.example.shopon.entity.User;
import com.example.shopon.exception.UnauthorizedAccessException;
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

        Role userRole = request.getRole() != null ? request.getRole() : Role.CUSTOMER;

        User user = User.Builder.user()
                .withFullName(request.getFullName())
                .withEmail(request.getEmail())
                .withPhoneNumber(request.getPhoneNumber())
                .withPassword(request.getPassword())
                .withRole(userRole)
                .build();
        return mapToResponse(userRepository.save(user));
    }

    public UserResponse getUserById(Long id, String currentUserEmail) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Authorization check: user can only view their own profile unless they're
        // admin
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Access denied: You can only view your own profile");
        }

        return mapToResponse(user);
    }

    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request, String currentUserEmail) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Authorization check: user can only update their own profile unless they're
        // admin
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Access denied: You can only update your own profile");
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id, String currentUserEmail) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Authorization check: user can only delete their own account unless they're
        // admin
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));

        if (!currentUser.getId().equals(id) && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Access denied: You can only delete your own account");
        }

        // Soft delete
        user.setActive(false);
        userRepository.save(user);
    }

    public void sendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is active
        if (!user.isActive()) {
            throw new RuntimeException("User account is inactive");
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        user.setOtp(otp);
        userRepository.save(user);

        System.out.println("OTP for " + email + " is: " + otp);
    }

    public void verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is active
        if (!user.isActive()) {
            throw new RuntimeException("User account is inactive");
        }

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
                .role(user.getRole())
                .build();
    }
}
