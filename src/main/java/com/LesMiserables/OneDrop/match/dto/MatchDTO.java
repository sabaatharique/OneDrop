package com.LesMiserables.OneDrop.match.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    private Long matchId;
    private Long requestId;
    private Long donorId;
    private String status;
    private LocalDateTime matchedAt;
}
