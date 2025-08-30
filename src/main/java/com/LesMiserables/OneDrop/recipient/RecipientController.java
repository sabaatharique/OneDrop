package com.LesMiserables.OneDrop.recipient;

import com.LesMiserables.OneDrop.recipient.dto.RecipientRequestDTO;
import com.LesMiserables.OneDrop.recipient.dto.RecipientResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipients")
public class RecipientController {
    private final RecipientService recipientService;

    public RecipientController(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @PostMapping
    public ResponseEntity<RecipientResponseDTO> addRecipient(@RequestBody RecipientRequestDTO request) {
        RecipientResponseDTO response = recipientService.addRecipient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecipientResponseDTO>> getAllRecipients() {
        List<RecipientResponseDTO> recipients = recipientService.getAllRecipients();
        return new ResponseEntity<>(recipients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipientResponseDTO> getRecipientById(@PathVariable Long id) {
        RecipientResponseDTO recipient = recipientService.getRecipientById(id);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipient(@PathVariable Long id) {
        recipientService.deleteRecipient(id);
        return new ResponseEntity<>("Donor deleted successfully", HttpStatus.OK);
    }
}
