package com.LesMiserables.OneDrop.match;

import com.LesMiserables.OneDrop.match.dto.DonorMatchDTO;
import com.LesMiserables.OneDrop.match.dto.RequestMatchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    // Donor views compatible requests with optional radius
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<DonorMatchDTO>> getMatchesForDonor(
            @PathVariable Long donorId,
            @RequestParam(defaultValue = "50") double radiusKm
    ) {
        List<DonorMatchDTO> matches = matchService.findMatchesForDonor(donorId, radiusKm);
        return ResponseEntity.ok(matches);
    }

    // Recipient views compatible donors with optional radius
    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<RequestMatchDTO>> getMatchesForRequest(
            @PathVariable Long requestId,
            @RequestParam(defaultValue = "50") double radiusKm
    ) {
        List<RequestMatchDTO> matches = matchService.findMatchesForRequest(requestId, radiusKm);
        return ResponseEntity.ok(matches);
    }
}
