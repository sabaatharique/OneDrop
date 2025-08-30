package com.LesMiserables.OneDrop.request;

import com.LesMiserables.OneDrop.request.dto.CreateRequestDTO;
import com.LesMiserables.OneDrop.request.dto.RequestDTO;
import com.LesMiserables.OneDrop.request.dto.UpdateRequestStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody CreateRequestDTO dto) {
        return ResponseEntity.ok(requestService.createRequest(dto));
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<RequestDTO>> getRequestsByRecipient(@PathVariable Long recipientId) {
        return ResponseEntity.ok(requestService.getRequestsByRecipient(recipientId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RequestDTO>> getPendingRequests() {
        return ResponseEntity.ok(requestService.getPendingRequests());
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<RequestDTO> updateStatus(
            @PathVariable Long requestId,
            @RequestBody UpdateRequestStatusDTO dto
    ) {
        return ResponseEntity.ok(requestService.updateStatus(requestId, dto));
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        requestService.deleteRequest(requestId);
        return ResponseEntity.noContent().build();
    }
}