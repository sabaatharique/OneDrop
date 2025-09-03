package com.LesMiserables.OneDrop.authentication;

import com.LesMiserables.OneDrop.authentication.dto.AuthRequest;
import com.LesMiserables.OneDrop.authentication.dto.AuthResponse;
import com.LesMiserables.OneDrop.authentication.dto.RegisterRequest;
import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.donor.DonorRepository;
import com.LesMiserables.OneDrop.exceptions.InvalidCredentialsException;
import com.LesMiserables.OneDrop.exceptions.UnauthorisedAdminActionException;
import com.LesMiserables.OneDrop.exceptions.UserNotFoundException;
import com.LesMiserables.OneDrop.recipient.Recipient;
import com.LesMiserables.OneDrop.recipient.RecipientRepository;
import com.LesMiserables.OneDrop.user.User;
import com.LesMiserables.OneDrop.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repo;
    private final JWTUtil jwtUtil;
    private final DonorRepository donorRepository;
    private final RecipientRepository recipientRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthService(UserRepository repo, JWTUtil jwtUtil, DonorRepository donorRepository, RecipientRepository recipientRepository) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
        this.donorRepository = donorRepository;
        this.recipientRepository = recipientRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getRole() == User.Role.ADMIN) {
            throw new UnauthorisedAdminActionException("Cannot register as ADMIN");
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
        Long userId = user.getId();
        Long recipientId = null;
        Long donorId = null;

        if (request.getRole() == User.Role.DONOR) {
            Donor donor = new Donor();
            donor.setUser(user);
            donor.setBloodType(request.getBloodType());
            donor.setLastDonationDate(request.getLastDonationDate());
            donorRepository.save(donor);
            donorId = donor.getId(); // Get donorId after saving
        } else if (request.getRole() == User.Role.RECIPIENT) {
            Recipient recipient = new Recipient();
            recipient.setUser(user);
            recipientRepository.save(recipient);
            recipientId = recipient.getId(); // Get recipientId after saving
        }

        return new AuthResponse(token, userId, recipientId, donorId);
    }

    public AuthResponse login(AuthRequest request) {
        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // validate password
        if (!passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        if (user.getRole() == User.Role.ADMIN) {
            throw new UnauthorisedAdminActionException("Admins cannot log in from this endpoint");
        }

        if ((user.getRole() == User.Role.DONOR && request.getRole() != User.Role.DONOR) ||
                (user.getRole() == User.Role.RECIPIENT && request.getRole() != User.Role.RECIPIENT)) {
            throw new UnauthorisedAdminActionException("This account is not registered as " + request.getRole().name());
        }

        if (user.getRole() == User.Role.BOTH &&
                (request.getRole() != User.Role.DONOR && request.getRole() != User.Role.RECIPIENT)) {
            throw new UnauthorisedAdminActionException("This account can only log in as DONOR or RECIPIENT");
        }

        String token = jwtUtil.generateToken(user.getEmail(), request.getRole().name());
        Long userId = user.getId();
        Long recipientId = null;
        Long donorId = null;

        if (user.getRole() == User.Role.RECIPIENT || user.getRole() == User.Role.BOTH) {
            recipientId = recipientRepository.findByUserId(userId)
                    .map(Recipient::getId)
                    .orElse(null);
        }
        if (user.getRole() == User.Role.DONOR || user.getRole() == User.Role.BOTH) {
            donorId = donorRepository.findByUserId(userId)
                    .map(Donor::getId)
                    .orElse(null);
        }
        return new AuthResponse(token, userId, recipientId, donorId);
    }
}
