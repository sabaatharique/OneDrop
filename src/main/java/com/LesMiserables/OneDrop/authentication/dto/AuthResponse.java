package com.LesMiserables.OneDrop.authentication.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private Long recipientId; // Add recipientId field
    private Long donorId;     // Add donorId field

    public AuthResponse(String token, Long userId, Long recipientId, Long donorId) {
        this.token = token;
        this.userId = userId;
        this.recipientId = recipientId;
        this.donorId = donorId;
    }
}
