package com.example.shopon.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "user")
@Entity
public class User extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private String otp;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    public static interface FullNameStep {
        EmailStep withFullName(String fullName);
    }

    public static interface EmailStep {
        PhoneNumberStep withEmail(String email);
    }

    public static interface PhoneNumberStep {
        PasswordStep withPhoneNumber(String phoneNumber);
    }

    public static interface PasswordStep {
        BuildStep withPassword(String password);
    }

    public static interface BuildStep {
        User build();
    }


    public static class Builder implements FullNameStep, EmailStep, PhoneNumberStep, PasswordStep, BuildStep {
        private String fullName;
        private String email;
        private String phoneNumber;
        private String password;

        private Builder() {
        }

        public static FullNameStep user() {
            return new Builder();
        }

        @Override
        public EmailStep withFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        @Override
        public PhoneNumberStep withEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public PasswordStep withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Override
        public BuildStep withPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public User build() {
            User user = new User();
            user.setFullName(this.fullName);
            user.setEmail(this.email);
            user.setPhoneNumber(this.phoneNumber);
            user.setPassword(this.password);
            return user;
        }
    }
}
