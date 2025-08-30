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

    // Donor views compatible requests
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<DonorMatchDTO>> getMatchesForDonor(@PathVariable Long donorId) {
        List<DonorMatchDTO> matches = matchService.findMatchesForDonor(donorId);
        return ResponseEntity.ok(matches);
    }

    // Recipient views compatible donors
    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<RequestMatchDTO>> getMatchesForRequest(@PathVariable Long requestId) {
        List<RequestMatchDTO> matches = matchService.findMatchesForRequest(requestId);
        return ResponseEntity.ok(matches);
    }

    // Donor updates match status: ACCEPTED, REJECTED, FULFILLED
    @PostMapping("/{matchId}/action")
    public ResponseEntity<Match> donorAction(
            @PathVariable Long matchId,
            @RequestParam Match.Status action
    ) {
        Match updatedMatch = matchService.handleDonorAction(matchId, action);
        return ResponseEntity.ok(updatedMatch);
    }

    // Recipient cancels a request
    @PostMapping("/request/{requestId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long requestId) {
        matchService.cancelRequest(requestId);
        return ResponseEntity.noContent().build();
    }
}