package com.fmi.springcourse.marketplace.user;

import com.fmi.springcourse.marketplace.user.dto.UserResponseDTO;
import com.fmi.springcourse.marketplace.user.dto.UserUpdateRequestDTO;
import com.fmi.springcourse.marketplace.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "<h1>Welcome</h1>";
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) { // fix to @AuthenticationPrincipal User
        return ResponseEntity.ok(userService.getUser(user.getEmail()));
    }

    @GetMapping("/admin")
    public String admin() {
        return "<h1>Welcome Admin</h1>";
    }

    @PatchMapping("/update")
    public UserResponseDTO updateUser(@AuthenticationPrincipal User user,
                                      @Valid @RequestBody UserUpdateRequestDTO request) {
        return userService.updateUser(user.getEmail(), request);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestHeader("Authorization") String authToken) {
        userService.deactivateUser(authToken);
    }
}
