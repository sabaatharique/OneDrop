package com.LesMiserables.OneDrop.request;

import com.LesMiserables.OneDrop.recipient.Recipient;
import com.LesMiserables.OneDrop.recipient.RecipientRepository;
import com.LesMiserables.OneDrop.request.dto.CreateRequestDTO;
import com.LesMiserables.OneDrop.request.dto.RequestDTO;
import com.LesMiserables.OneDrop.request.dto.UpdateRequestStatusDTO;
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

    public RequestDTO updateStatus(Long requestId, UpdateRequestStatusDTO dto) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(dto.getStatus());
        Request updated = requestRepo.save(request);

        return mapToDto(updated);
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
                request.getCreatedAt()
        );
    }
}

