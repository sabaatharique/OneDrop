package com.LesMiserables.OneDrop.request;

import com.LesMiserables.OneDrop.request.dto.CreateRequestDTO;
import com.LesMiserables.OneDrop.request.dto.RequestDTO;
import com.LesMiserables.OneDrop.request.dto.UpdateRequestDTO;
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

    @GetMapping("/recipient/{donorId}")
    public ResponseEntity<List<RequestDTO>> getRequestsByDonor(@PathVariable Long donorId) {
        return ResponseEntity.ok(requestService.getRequestsByDonor(donorId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RequestDTO>> getPendingRequests() {
        return ResponseEntity.ok(requestService.getPendingRequests());
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<RequestDTO> updateRequest(
            @PathVariable Long requestId,
            @RequestBody UpdateRequestDTO dto
    ) {
        return ResponseEntity.ok(requestService.updateRequest(requestId, dto));
    }

    @PutMapping("/{requestId}/accept")
    public ResponseEntity<RequestDTO> acceptRequest(@PathVariable Long requestId, @RequestParam Long donorID) {
        return ResponseEntity.ok(requestService.acceptRequest(requestId, donorID));
    }

    @PutMapping("/{requestId}/complete")
    public ResponseEntity<RequestDTO> completeRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.completeRequest(requestId));
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<RequestDTO> rejectRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.rejectRequest(requestId));
    }

    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDTO> cancelRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.cancelRequest(requestId));
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        requestService.deleteRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/donor/{donorId}/upcoming")
    public ResponseEntity<List<Request>> getUpcomingForDonor(@PathVariable Long donorId) {
        List<Request> requests = requestService.getUpcomingRequestsForDonor(donorId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/donor/{donorId}/history")
    public ResponseEntity<List<Request>> getDonationHistory(@PathVariable Long donorId) {
        List<Request> requests = requestService.getDonationHistoryForDonor(donorId);
        return ResponseEntity.ok(requests);
    }
}