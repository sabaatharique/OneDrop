package com.LesMiserables.OneDrop.authentication.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRole {
    String message() default "ADMIN registration not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
