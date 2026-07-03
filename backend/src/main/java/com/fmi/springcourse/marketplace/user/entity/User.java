package com.fmi.springcourse.marketplace.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "userId", updatable = false, nullable = false)
	private UUID id;
	
	@NotBlank
	@Column(name = "profileName", nullable = false, unique = true)
	private String profileName;
	
	@Email
	@NotBlank
	@Column(nullable = false, unique = true)
	private String email;
	
	//    @Pattern(
//            regexp = "^(?=.*[0-9])(?=.*[a-z]).{8,}$"
//    )
	@Column(nullable = false)
	private String password; // will store the hashed password
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@Column(name = "active")
	private Boolean active = true; // for user deletion purposes
	
	private void validate(String profileName, String email, String password, UserRole role) {
		if (profileName == null || profileName.isBlank()) {
			throw new IllegalArgumentException("Profile name cannot be null or empty");
		}
		
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be null or empty");
		}
		
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}
		
		if (role == null) {
			throw new IllegalArgumentException("Role cannot be null");
		}
	}
	
	public User(String profileName, String email, String password, UserRole role) {
		validate(profileName, email, password, role);
		
		this.profileName = profileName;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
	
	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", profileName='" + profileName + '\'' +
			", email='" + email + '\'' +
			", role=" + role +
			'}';
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}
	
	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isEnabled() {
		return active;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
