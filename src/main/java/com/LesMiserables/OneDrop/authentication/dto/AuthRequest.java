package com.LesMiserables.OneDrop.authentication.dto;

import com.LesMiserables.OneDrop.authentication.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must be 8 characters long")
    private String password;

    @NotBlank(message = "Role is required")
    private User.Role role;
}
