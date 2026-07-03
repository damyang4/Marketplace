package com.fmi.springcourse.marketplace.user;

import com.fmi.springcourse.marketplace.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByProfileName(String profileName);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
	
	Optional<User> findByProfileName(String profileName);
}
