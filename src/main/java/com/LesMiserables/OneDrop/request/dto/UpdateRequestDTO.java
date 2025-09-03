package com.LesMiserables.OneDrop.request.dto;

import com.LesMiserables.OneDrop.request.Request;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateRequestDTO {
    private Request.Status status;
    private LocalDateTime requiredBy;
}