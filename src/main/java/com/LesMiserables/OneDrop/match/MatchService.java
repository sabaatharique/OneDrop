package com.LesMiserables.OneDrop.match;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.donor.DonorRepository;
import com.LesMiserables.OneDrop.location.Location;
import com.LesMiserables.OneDrop.location.LocationUtil;
import com.LesMiserables.OneDrop.match.dto.DonorMatchDTO;
import com.LesMiserables.OneDrop.match.dto.RequestMatchDTO;
import com.LesMiserables.OneDrop.request.Request;
import com.LesMiserables.OneDrop.request.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final RequestRepository requestRepo;
    private final DonorRepository donorRepo;
    private final MatchRepository matchRepo;
    private final LocationUtil locUtil;

    // Donor views compatible requests nearby
    public List<DonorMatchDTO> findMatchesForDonor(Long donorId, Location donorLocation, double radiusKm, String bloodGroup) {
        Donor donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        if (donorLocation == null) throw new RuntimeException("Donor location not set");

        List<Request> pendingRequests = requestRepo.findByStatus(Request.Status.PENDING);

        return pendingRequests.stream()
                // filter by blood compatibility
                .filter(req -> isBloodCompatible(donor.getBloodType(), req.getBloodType()))
                // filter by bloodGroup if provided
                .filter(req -> bloodGroup == null || bloodGroup.isEmpty() || req.getBloodType().equalsIgnoreCase(bloodGroup))
                // filter by distance within radius
                .filter(req -> locUtil.distance(donorLocation, req.getLocation()) <= radiusKm)
                // sort by distance first, then urgency
                .sorted(Comparator
                        .comparing((Request req) -> locUtil.distance(donorLocation, req.getLocation()))
                        .thenComparing(Request::getRequiredBy))
                .map(req -> new DonorMatchDTO(
                        req.getId(),
                        req.getRecipient().getUser().getFullName(),
                        req.getPatientName(),
                        req.getBloodType(),
                        req.getLocation(),
                        req.getRequiredBy(),
                        locUtil.distance(donorLocation, req.getLocation()),
                        req.getRecipientPhone()
                ))
                .distinct()
                .toList();
    }

    // Recipient views compatible donors nearby
    public List<RequestMatchDTO> findMatchesForRequest(Long requestId, Location donorLocation, double radiusKm) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Location requestLocation = request.getLocation();
        if (requestLocation == null) throw new RuntimeException("Request location not set");

        List<Donor> donors = donorRepo.findAll();

        return donors.stream()
                .filter(donor -> isBloodCompatible(donor.getBloodType(), request.getBloodType()))
                .filter(donor -> donor.isEligibleToDonate())
                .filter(donor -> locUtil.distance(donorLocation, requestLocation) <= radiusKm)
                .sorted(Comparator
                        .comparing((Donor donor) -> locUtil.distance(donorLocation, requestLocation)))
                .map(donor -> new RequestMatchDTO(
                        donor.getId(),
                        donor.getUser().getFullName(),
                        donor.getBloodType(),
                        donorLocation,
                        locUtil.distance(donorLocation, requestLocation)
                ))
                .distinct()
                .toList();
    }

    // Create a match (donor accepts request)
    @Transactional
    public Match acceptRequest(Long donorId, Long requestId) {
        Donor donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!donor.isEligibleToDonate()) {
            throw new RuntimeException("Donor not eligible");
        }
        if (request.getStatus() != Request.Status.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        // create match
        Match match = new Match();
        match.setDonor(donor);
        match.setRequest(request);
        match.setMatchedAt(LocalDateTime.now());
        matchRepo.save(match);

        // update request status
        request.setStatus(Request.Status.MATCHED);
        requestRepo.save(request);

        return match;
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
            case "AB-" -> donorBlood.equalsIgnoreCase("O-") || donorBlood.equalsIgnoreCase("A-")
                    || donorBlood.equalsIgnoreCase("B-") || donorBlood.equalsIgnoreCase("AB-");
            case "AB+" -> true;
            default -> false;
        };
    }
}

