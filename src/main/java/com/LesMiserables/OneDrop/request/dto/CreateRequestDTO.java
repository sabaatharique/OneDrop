package com.LesMiserables.OneDrop.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRequestDTO {
    private Long recipientId;
    private String bloodType;
    private String location;
    private LocalDateTime requiredBy;
}