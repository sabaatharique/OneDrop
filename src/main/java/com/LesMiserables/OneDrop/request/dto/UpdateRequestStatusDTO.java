package com.LesMiserables.OneDrop.request.dto;

import com.LesMiserables.OneDrop.request.Request;
import lombok.Data;

@Data
public class UpdateRequestStatusDTO {
    private Request.Status status;
}
