package com.LesMiserables.OneDrop.match.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMatchDTO {
    private Long donorId;
    private String donorName;
    private String bloodType;
    private String location;
    private double distanceKm;
}

