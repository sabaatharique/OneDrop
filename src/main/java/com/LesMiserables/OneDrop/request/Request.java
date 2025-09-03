package com.LesMiserables.OneDrop.request;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.location.Location;
import com.LesMiserables.OneDrop.recipient.Recipient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Recipient recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private Donor matchedDonor;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String bloodType;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private LocalDateTime requiredBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        MATCHED,
        FULFILLED,
        CANCELLED,
        EXPIRED
    }
}