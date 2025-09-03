package com.LesMiserables.OneDrop.request.dto;

import com.LesMiserables.OneDrop.location.Location;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime; // Keep this import for now, might be needed for other DTOs or if we decide to parse here

@Data
public class CreateRequestDTO {
    private Long recipientId;
    private String patientName;
    private String bloodType;
    private Location location;
    private String hospital;
    private LocalDateTime requiredBy;
}