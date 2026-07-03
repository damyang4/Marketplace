package com.fmi.springcourse.marketplace.user.dto;

import com.fmi.springcourse.marketplace.user.entity.User;
import com.fmi.springcourse.marketplace.user.entity.UserRole;

import java.util.UUID;

public record UserResponseDTO(UUID id,
                              String profileName,
                              String email,
                              UserRole role,
                              boolean active) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getProfileName(), user.getEmail(), user.getRole(), user.getActive());
    }
}
