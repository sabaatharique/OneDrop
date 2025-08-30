package com.LesMiserables.OneDrop.authentication;

import com.LesMiserables.OneDrop.authentication.dto.AuthRequest;
import com.LesMiserables.OneDrop.authentication.dto.AuthResponse;
import com.LesMiserables.OneDrop.authentication.dto.RegisterRequest;
import com.LesMiserables.OneDrop.authentication.exception.InvalidCredentialsException;
import com.LesMiserables.OneDrop.authentication.exception.UnauthorisedActionException;
import com.LesMiserables.OneDrop.authentication.exception.UserNotFoundException;
import com.LesMiserables.OneDrop.user.User;
import com.LesMiserables.OneDrop.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repo;
    private final JWTUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthService(UserRepository repo, JWTUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getRole() == User.Role.ADMIN) {
            throw new UnauthorisedActionException("Cannot register as ADMIN");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder().encode(request.getPassword()))
                .role(request.getRole())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .build();

        repo.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        var user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // check user role during login
        if (user.getRole() != request.getRole()) {
            throw new UnauthorisedActionException(
                    "You tried to log in as " + request.getRole() +
                    " but your account is registered as " + user.getRole()
            );
        }

        if (!passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token);
    }
}
