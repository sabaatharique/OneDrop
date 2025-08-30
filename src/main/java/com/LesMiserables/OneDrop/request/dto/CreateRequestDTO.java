package com.LesMiserables.OneDrop.request.dto;

import lombok.Data;

@Data
public class CreateRequestDTO {
    private Long recipientId;
    private String bloodType;
    private String location;
}