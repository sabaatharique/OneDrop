package com.LesMiserables.OneDrop.authentication;

import com.LesMiserables.OneDrop.authentication.dto.AuthRequest;
import com.LesMiserables.OneDrop.authentication.dto.AuthResponse;
import com.LesMiserables.OneDrop.authentication.dto.RegisterRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
