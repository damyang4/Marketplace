package com.fmi.springcourse.marketplace.auth;

import com.fmi.springcourse.marketplace.auth.dto.AuthResponse;
import com.fmi.springcourse.marketplace.auth.dto.LoginRequest;
import com.fmi.springcourse.marketplace.auth.dto.RegistrationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        AuthResponse user = authService.register(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        AuthResponse user = authService.login(request);
        return ResponseEntity.ok(user);
    }
}
