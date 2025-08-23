package com.LesMiserables.OneDrop.authentication.validation;

import com.LesMiserables.OneDrop.authentication.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidUserRole, User.Role> {
    @Override
    public boolean isValid(User.Role role, ConstraintValidatorContext context) {
        // only allow DONOR or RECIPIENT
        return role != null && role != User.Role.ADMIN;
    }
}
