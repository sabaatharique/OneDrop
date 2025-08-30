package com.LesMiserables.OneDrop.recipient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipientResponseDTO {
    private Long id;
    private String bloodType;
    private String city;
    private Long userId;
}
