package com.LesMiserables.OneDrop.request;

import com.LesMiserables.OneDrop.recipient.Recipient;
import com.LesMiserables.OneDrop.recipient.RecipientRepository;
import com.LesMiserables.OneDrop.request.dto.CreateRequestDTO;
import com.LesMiserables.OneDrop.request.dto.RequestDTO;
import com.LesMiserables.OneDrop.request.dto.UpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepo;
    private final RecipientRepository recipientRepo;

    public RequestDTO createRequest(CreateRequestDTO dto) {
        Recipient recipient = recipientRepo.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Request request = new Request();
        request.setRecipient(recipient);
        request.setBloodType(dto.getBloodType());
        request.setLocation(dto.getLocation());
        request.setStatus(Request.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setRequiredBy(dto.getRequiredBy());

        Request saved = requestRepo.save(request);
        return mapToDto(saved);
    }

    public List<RequestDTO> getRequestsByRecipient(Long recipientId) {
        return requestRepo.findByRecipientId(recipientId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<RequestDTO> getPendingRequests() {
        return requestRepo.findByStatus(Request.Status.PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public RequestDTO updateRequest(Long requestId, UpdateRequestDTO dto) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (dto.getStatus() != null) {
            request.setStatus(dto.getStatus());
        }

        if (dto.getRequiredBy() != null) {
            request.setRequiredBy(dto.getRequiredBy());
        }

        return mapToDto(requestRepo.save(request));
    }

    public RequestDTO acceptRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != Request.Status.PENDING) {
            throw new RuntimeException("Only pending requests can be accepted");
        }

        request.setStatus(Request.Status.MATCHED);
        return mapToDto(requestRepo.save(request));
    }

    public RequestDTO completeRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != Request.Status.MATCHED) {
            throw new RuntimeException("Only matched requests can be marked fulfilled");
        }

        request.setStatus(Request.Status.FULFILLED);
        return mapToDto(requestRepo.save(request));
    }

    public RequestDTO rejectRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != Request.Status.MATCHED) {
            throw new RuntimeException("Only matched requests can be rejected by donor");
        }

        request.setStatus(Request.Status.PENDING);
        return mapToDto(requestRepo.save(request));
    }

    public RequestDTO cancelRequest(Long requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() == Request.Status.FULFILLED || request.getStatus() == Request.Status.EXPIRED) {
            throw new RuntimeException("Cannot cancel a fulfilled or expired request");
        }

        request.setStatus(Request.Status.CANCELLED);
        return mapToDto(requestRepo.save(request));
    }


    public void deleteRequest(Long requestId) {
        if (!requestRepo.existsById(requestId)) {
            throw new RuntimeException("Request not found");
        }
        requestRepo.deleteById(requestId);
    }

    private RequestDTO mapToDto(Request request) {
        return new RequestDTO(
                request.getId(),
                request.getRecipient().getId(),
                request.getRecipient().getUser().getFullName(),
                request.getBloodType(),
                request.getLocation(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getRequiredBy()
        );
    }

    public void updateExpiredRequests() {
        LocalDateTime now = LocalDateTime.now();
        List<Request> expired = requestRepo.findByStatusAndRequiredByBefore(Request.Status.PENDING, now);
        expired.forEach(r -> r.setStatus(Request.Status.EXPIRED));
        requestRepo.saveAll(expired);
    }
}