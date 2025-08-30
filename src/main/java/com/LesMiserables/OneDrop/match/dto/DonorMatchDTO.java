package com.LesMiserables.OneDrop.match.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorMatchDTO {
    private Long requestId;
    private String recipientName;
    private String bloodType;
    private String location;
    private LocalDateTime requiredBy;
    private double distanceKm;   // optional, if you calculate distance
}
