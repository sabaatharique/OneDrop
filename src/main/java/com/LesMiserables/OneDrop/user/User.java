package com.LesMiserables.OneDrop.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        DONOR,
        RECIPIENT,
        BOTH,
        ADMIN
    }

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String phone;
}
