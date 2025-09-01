package com.LesMiserables.OneDrop.match.dto;

import com.LesMiserables.OneDrop.location.Location;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMatchDTO {
    private Long donorId;
    private String donorName;
    private String bloodType;
    private Location location;
    private double distanceKm;
}

