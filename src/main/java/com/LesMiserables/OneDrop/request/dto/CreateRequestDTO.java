package com.LesMiserables.OneDrop.request.dto;

import com.LesMiserables.OneDrop.location.Location;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRequestDTO {
    private Long recipientId;
    private String bloodType;
    private Location location;
    private LocalDateTime requiredBy;
}