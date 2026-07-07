package com.fmi.springcourse.marketplace.auth;

import com.fmi.springcourse.marketplace.auth.dto.AuthResponse;
import com.fmi.springcourse.marketplace.auth.dto.LoginRequest;
import com.fmi.springcourse.marketplace.auth.dto.RegistrationRequest;
import com.fmi.springcourse.marketplace.exception.UserAlreadyExistsException;
import com.fmi.springcourse.marketplace.exception.UserNotFoundException;
import com.fmi.springcourse.marketplace.user.UserRepository;
import com.fmi.springcourse.marketplace.user.entity.User;
import com.fmi.springcourse.marketplace.user.entity.UserRole;
import com.fmi.springcourse.marketplace.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private User mapToUser(RegistrationRequest dto) {
        return new User(
                dto.profileName(),
                dto.email(),
                passwordEncoder.encode(dto.password()),
                UserRole.USER
        );
    }

    @Transactional
    public AuthResponse register(RegistrationRequest request) {
        if (repo.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        if (repo.existsByProfileName(request.profileName())) {
            throw new UserAlreadyExistsException("User with this username already exists.");
        }

        User user = mapToUser(request);
        repo.save(user);

        String jwt = jwtService.generateToken(user.getUsername());
        return new AuthResponse(jwt, user.getProfileName());
    }

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String jwt = jwtService.generateToken(request.email());

        User user = repo.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User with email " + request.email() + " was not found."));
        return new AuthResponse(jwt, user.getProfileName());
    }
}
