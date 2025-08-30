package com.LesMiserables.OneDrop.request;

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

    @Column(nullable = false)
    private String bloodType;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        MATCHED,
        FULFILLED,
        CANCELLED
    }
}
