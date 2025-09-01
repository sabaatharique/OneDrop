package com.LesMiserables.OneDrop.match.dto;

import com.LesMiserables.OneDrop.location.Location;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorMatchDTO {
    private Long requestId;
    private String recipientName;
    private String bloodType;
    private Location location;
    private LocalDateTime requiredBy;
    private double distanceKm;
}
