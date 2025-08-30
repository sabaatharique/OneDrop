package com.LesMiserables.OneDrop.request.dto;

import com.LesMiserables.OneDrop.request.Request;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDTO {
    private Long id;
    private Long recipientId;
    private String recipientName;
    private String bloodType;
    private String location;
    private Request.Status status;
    private LocalDateTime createdAt;
}
