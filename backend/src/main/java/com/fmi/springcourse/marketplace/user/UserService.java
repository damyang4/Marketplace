package com.fmi.springcourse.marketplace.user;

import com.fmi.springcourse.marketplace.exception.UserNotActiveException;
import com.fmi.springcourse.marketplace.exception.UserNotFoundException;
import com.fmi.springcourse.marketplace.user.dto.UserResponseDTO;
import com.fmi.springcourse.marketplace.user.dto.UserUpdateRequestDTO;
import com.fmi.springcourse.marketplace.user.entity.User;
import com.fmi.springcourse.marketplace.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final JwtService jwtService;

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getProfileName(), user.getEmail(), user.getRole(), user.getActive());
    }

    private Optional<User> findUserByToken(String authToken) {
        String parsedToken = authToken.substring(7);
        String email = jwtService.extractUsername(parsedToken);
        return repo.findByEmail(email);
    }

    private User findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with email: " + email + " was not found"));
    }

    public UserResponseDTO getUser(String email) {
        return mapToResponseDTO(findByEmail(email));
    }

    @Transactional
    public UserResponseDTO updateUser(String email, UserUpdateRequestDTO request) {
        User user = findByEmail(email);

        if (!user.getActive()) {
            throw new UserNotActiveException("Cannot update an inactive user account");
        }

        if (request.profileName() != null) user.setProfileName(request.profileName());
        if (request.email() != null) user.setEmail(request.email());

        // No need for this line when the @Transaction is used
        User updated = repo.save(user);
        return mapToResponseDTO(updated);
    }

    @Transactional
    public void deactivateUser(String authToken) {
        User user = findUserByToken(authToken)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));

        if (!user.getActive()) {
            throw new UserNotActiveException("User already deleted");
        }

        user.setActive(false);
        repo.save(user);

        // should handle all items that this user have created (they should be also deactivated)
    }
}
