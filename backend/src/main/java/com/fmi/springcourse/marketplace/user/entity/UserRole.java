package com.fmi.springcourse.marketplace.user.entity;

public enum UserRole {
    // MODERATOR ?
    ADMIN,
    USER;

    public static UserRole parse(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        return UserRole.valueOf(role.toUpperCase());
    }
}
