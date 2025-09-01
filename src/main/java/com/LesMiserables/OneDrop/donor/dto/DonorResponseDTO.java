package com.LesMiserables.OneDrop.donor.dto;

import com.LesMiserables.OneDrop.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DonorResponseDTO {
    private Long id;
    private String bloodType;
    private boolean eligibleToDonate;
    private Long userId;
    private Location location;
}
