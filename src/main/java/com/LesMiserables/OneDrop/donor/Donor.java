package com.LesMiserables.OneDrop.donor;

import com.LesMiserables.OneDrop.authentication.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "donors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bloodType;

    @Column(nullable = false)
    private String city;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
