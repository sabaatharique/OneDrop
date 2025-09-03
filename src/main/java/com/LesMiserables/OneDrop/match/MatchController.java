package com.LesMiserables.OneDrop.match;

import com.LesMiserables.OneDrop.location.Location;
import com.LesMiserables.OneDrop.match.dto.DonorMatchDTO;
import com.LesMiserables.OneDrop.match.dto.RequestMatchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            @RequestParam String address,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "50") double radiusKm,
            @RequestParam(required = false) String bloodGroup
    ) {
        Location donorLocation = new Location(latitude, longitude, address);
        List<DonorMatchDTO> matches = matchService.findMatchesForDonor(donorId, donorLocation, radiusKm, bloodGroup);
        return ResponseEntity.ok(matches);
    }

    // Recipient views compatible donors with optional radius
    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<RequestMatchDTO>> getMatchesForRequest(
            @PathVariable Long requestId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam String address,
            @RequestParam(defaultValue = "10") double radiusKm
    ) {
        Location donorLocation = new Location(latitude, longitude, address);
        List<RequestMatchDTO> matches = matchService.findMatchesForRequest(requestId, donorLocation, radiusKm);
        return ResponseEntity.ok(matches);
    }
}
