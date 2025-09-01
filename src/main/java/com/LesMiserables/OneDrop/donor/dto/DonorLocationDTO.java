package com.LesMiserables.OneDrop.donor.dto;

import com.LesMiserables.OneDrop.location.Location;
import lombok.Data;

@Data
public class DonorLocationDTO {
    private Location location;
    private double radiusKm;
}
