package com.LesMiserables.OneDrop.recipient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipientResponseDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
}
