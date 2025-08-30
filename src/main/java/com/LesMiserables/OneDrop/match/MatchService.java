package com.LesMiserables.OneDrop.match;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.donor.DonorRepository;
import com.LesMiserables.OneDrop.match.dto.DonorMatchDTO;
import com.LesMiserables.OneDrop.match.dto.RequestMatchDTO;
import com.LesMiserables.OneDrop.request.Request;
import com.LesMiserables.OneDrop.request.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final RequestRepository requestRepo;
    private final DonorRepository donorRepo;
    private final MatchRepository matchRepo;

    // Donor views compatible requests
    public List<DonorMatchDTO> findMatchesForDonor(Long donorId) {
        Donor donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        List<Request> pendingRequests = requestRepo.findByStatus(Request.Status.PENDING);

        return pendingRequests.stream()
                .filter(req -> isCompatible(donor, req))
                .sorted(Comparator.comparing(Request::getRequiredBy)) // sort by urgency
                .map(req -> new DonorMatchDTO(
                        req.getId(),
                        req.getRecipient().getUser().getFullName(),
                        req.getBloodType(),
                        req.getLocation(),
                        req.getRequiredBy(),
                        0.0
                ))
                .toList();
    }

    // Recipient views compatible donors
    public List<RequestMatchDTO> findMatchesForRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        List<Donor> donors = donorRepo.findAll();

        return donors.stream()
                .filter(donor -> isCompatible(donor, request))
                .map(donor -> new RequestMatchDTO(
                        donor.getId(),
                        donor.getUser().getFullName(),
                        donor.getBloodType(),
                        donor.getCity(),
                        0.0
                ))
                .toList();
    }


    // Donor action: ACCEPT, REJECT, FULFILLED
    public Match handleDonorAction(Long matchId, Match.Status action) {
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setStatus(action);
        matchRepo.save(match);

        updateRequestStatusBasedOnMatches(match.getRequest());

        return match;
    }

    // Recipient cancels request
    public void cancelRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(Request.Status.CANCELLED);
        requestRepo.save(request);

        matchRepo.findByRequest(request).forEach(match -> {
            match.setStatus(Match.Status.CANCELLED);
            matchRepo.save(match);
        });
    }

    // update request based on matches
    private void updateRequestStatusBasedOnMatches(Request request) {
        List<Match> matches = matchRepo.findByRequest(request);

        if (matches.isEmpty()) {
            request.setStatus(Request.Status.PENDING);
        } else {
            boolean anyAccepted = matches.stream()
                    .anyMatch(m -> m.getStatus() == Match.Status.ACCEPTED);
            boolean allFulfilled = matches.stream()
                    .allMatch(m -> m.getStatus() == Match.Status.FULFILLED);
            boolean allCancelledOrRejected = matches.stream()
                    .allMatch(m -> m.getStatus() == Match.Status.CANCELLED || m.getStatus() == Match.Status.REJECTED);

            if (allFulfilled) {
                request.setStatus(Request.Status.FULFILLED);
            } else if (anyAccepted) {
                request.setStatus(Request.Status.MATCHED);
            } else if (allCancelledOrRejected) {
                request.setStatus(Request.Status.PENDING);
            } else {
                request.setStatus(Request.Status.PENDING);
            }
        }

        requestRepo.save(request);
    }

    private boolean isCompatible(Donor donor, Request request) {
        return isBloodCompatible(donor.getBloodType(), request.getBloodType())
                && isCityMatching(donor.getCity(), request.getLocation());
    }

    private boolean isBloodCompatible(String donorBlood, String recipientBlood) {
        return switch (recipientBlood.toUpperCase()) {
            case "O-" -> donorBlood.equalsIgnoreCase("O-");
            case "O+" -> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("O+");
            case "A-" -> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("A-");
            case "A+" -> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("O+")
                    || donorBlood.equalsIgnoreCase("A-") || donorBlood.equalsIgnoreCase("A+");
            case "B-" -> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("B-");
            case "B+" -> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("O+")
                    || donorBlood.equalsIgnoreCase("B-") || donorBlood.equalsIgnoreCase("B+");
            case "AB-"-> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("A-")
                    || donorBlood.equalsIgnoreCase("B-") || donorBlood.equalsIgnoreCase("AB-");
            case "AB+" -> true;
            default -> false;
        };
    }

    private boolean isCityMatching(String donorCity, String requestCity) {
        return donorCity.equalsIgnoreCase(requestCity);
    }
}