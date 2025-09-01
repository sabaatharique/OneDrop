package com.LesMiserables.OneDrop.donations;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.recipient.Recipient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "donation_appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationAppointment {

    public enum Status {
        PENDING, COMPLETED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Recipient recipient;

    @Column(nullable = false)
    private String hospital;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
