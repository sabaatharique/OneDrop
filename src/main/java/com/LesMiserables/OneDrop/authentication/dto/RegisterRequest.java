package com.LesMiserables.OneDrop.authentication.dto;

import com.LesMiserables.OneDrop.user.User.Role;
import com.LesMiserables.OneDrop.authentication.validation.ValidUserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterRequest {
    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "Password required")
    private String password;

    @NotBlank(message = "Full name required")
    private String fullName;

    // user can only register as DONOR or RECIPIENT
    @NotBlank(message = "Role required")
    @ValidUserRole
    private Role role;

    @NotBlank(message = "Phone number required")
    private String phone;
}