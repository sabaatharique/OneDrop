package com.LesMiserables.OneDrop.request;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.donor.DonorRepository;
import com.LesMiserables.OneDrop.exceptions.*;
import com.LesMiserables.OneDrop.recipient.Recipient;
import com.LesMiserables.OneDrop.recipient.RecipientRepository;
import com.LesMiserables.OneDrop.request.dto.CreateRequestDTO;
import com.LesMiserables.OneDrop.request.dto.RequestDTO;
import com.LesMiserables.OneDrop.request.dto.UpdateRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepo;
    private final RecipientRepository recipientRepo;
    private final DonorRepository donorRepo;

    // recipient creates request: PENDING
    public RequestDTO createRequest(CreateRequestDTO dto) {
        Recipient recipient = recipientRepo.findById(dto.getRecipientId())
                .orElseThrow(() -> new RecipientNotFoundException("Recipient with id " + dto.getRecipientId() + " not found"));

        Request request = new Request();
        request.setRecipient(recipient);
        request.setBloodType(dto.getBloodType());
        request.setLocation(dto.getLocation());
        request.setStatus(Request.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setRequiredBy(dto.getRequiredBy());
        request.setMatchedDonor(null);

        Request saved = requestRepo.save(request);
        return mapToDto(saved);
    }

    // find all requests by a recipient
    public List<RequestDTO> getRequestsByRecipient(Long recipientId) {
        return requestRepo.findByRecipientId(recipientId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<RequestDTO> getRequestsByDonor(Long donorId) {
        return requestRepo.findByDonorId(donorId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // get all pending requests
    public List<RequestDTO> getPendingRequests() {
        return requestRepo.findByStatus(Request.Status.PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // update status or required by time for a request
    public RequestDTO updateRequest(Long requestId, UpdateRequestDTO dto) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request with id " + requestId + " not found"));

        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
        }

        if (dto.getRequiredBy() != null) {
            request.setRequiredBy(dto.getRequiredBy());
        }

        return mapToDto(requestRepo.save(request));
    }

    // delete request from database
    public void deleteRequest(Long requestId) {
        if (!requestRepo.existsById(requestId)) {
            throw new RequestNotFoundException("Request not found");
        }
        requestRepo.deleteById(requestId);
    }

    // donor accepts request, matchedDonor added to request: PENDING -> MATCHED
    @Transactional
    public RequestDTO acceptRequest(Long requestId, Long donorId) {
        Donor donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new DonorNotFoundException("Donor with id " + donorId + " not found"));

        if(!donor.isEligibleToDonate()) {
            throw new DonorNotEligibleException("Donor not eligible to donate");
        }

        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        if (request.getStatus() != Request.Status.PENDING) {
            throw new InvalidRequestActionException("Only pending requests can be accepted");
        }

        request.setMatchedDonor(donor);
        request.setStatus(Request.Status.MATCHED);
        return mapToDto(requestRepo.save(request));
    }

    // request fulfilled, donor's last donation set to now: MATCHED -> FULFILLED
    @Transactional
    public RequestDTO completeRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        if (request.getStatus() != Request.Status.MATCHED) {
            throw new InvalidRequestActionException("Only matched requests can be marked fulfilled");
        }

        Donor donor = request.getMatchedDonor();
        if (donor == null) {
            throw new DonorNotFoundException("No donor matched to this request");
        }
        donor.setLastDonationDate(LocalDate.now());
        donorRepo.save(donor);

        request.setStatus(Request.Status.FULFILLED);
        return mapToDto(requestRepo.save(request));
    }

    // donor rejects request: MATCHED -> PENDING
    @Transactional
    public RequestDTO rejectRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        if (request.getStatus() != Request.Status.MATCHED) {
            throw new InvalidRequestActionException("Only matched requests can be rejected by donor");
        }

        request.setMatchedDonor(null);
        request.setStatus(Request.Status.PENDING);
        return mapToDto(requestRepo.save(request));
    }

    // recipient cancels request: PENDING/MATCHED -> CANCELLED
    @Transactional
    public RequestDTO cancelRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        if (request.getStatus() == Request.Status.FULFILLED || request.getStatus() == Request.Status.EXPIRED) {
            throw new InvalidRequestActionException("Cannot cancel a fulfilled or expired request");
        }

        request.setMatchedDonor(null);
        request.setStatus(Request.Status.CANCELLED);
        return mapToDto(requestRepo.save(request));
    }

    // show upcoming accepted/matched requests for donor
    public List<Request> getUpcomingRequestsForDonor(Long donorId) {
        return requestRepo.findByMatchedDonorIdAndStatus(
                donorId,
                Request.Status.MATCHED
        );
    }

    // fulfilled donation history for donor
    public List<Request> getDonationHistoryForDonor(Long donorId) {
        return requestRepo.findByMatchedDonorIdAndStatus(
                donorId,
                Request.Status.FULFILLED
        );
    }

    private RequestDTO mapToDto(Request request) {
        return new RequestDTO(
                request.getId(),
                request.getRecipient().getId(),
                request.getRecipient().getUser().getFullName(),
                request.getMatchedDonor() != null ? request.getMatchedDonor().getId() : null,
                request.getMatchedDonor() != null ? request.getMatchedDonor().getUser().getFullName() : null,
                request.getBloodType(),
                request.getLocation(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getRequiredBy()
        );
    }

}