package com.LesMiserables.OneDrop.donor;

import com.LesMiserables.OneDrop.location.Location;
import com.LesMiserables.OneDrop.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(nullable = true)
    private LocalDate lastDonationDate;

    @Transient
    public boolean isEligibleToDonate() {
        return lastDonationDate == null || lastDonationDate.isBefore(LocalDate.now().minusDays(90));
    }

    @Transient
    private Location location;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
