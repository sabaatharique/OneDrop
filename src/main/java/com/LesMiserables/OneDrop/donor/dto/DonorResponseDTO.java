package com.LesMiserables.OneDrop.donor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DonorResponseDTO {
    private Long id;           // Donor ID
    private String bloodType;
    private String city;
    private Long userId;       // ID of the associated User
}
