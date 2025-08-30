package com.LesMiserables.OneDrop.recipient;

import com.LesMiserables.OneDrop.exceptions.RecipientNotFoundException;
import com.LesMiserables.OneDrop.recipient.dto.RecipientRequestDTO;
import com.LesMiserables.OneDrop.recipient.dto.RecipientResponseDTO;
import com.LesMiserables.OneDrop.user.User;
import com.LesMiserables.OneDrop.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipientService {
    private final RecipientRepository recipientRepository;
    private final UserRepository userRepository;

    public RecipientService(RecipientRepository recipientRepository, UserRepository userRepository) {
        this.recipientRepository = recipientRepository;
        this.userRepository = userRepository;
    }

    // add new recipient
    public RecipientResponseDTO addRecipient(RecipientRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RecipientNotFoundException("Recipient not found with id: " + request.getUserId()));

        Recipient recipient = Recipient.builder()
                .bloodType(request.getBloodType())
                .city(request.getCity())
                .user(user)
                .build();

        Recipient saved = recipientRepository.save(recipient);

        return new RecipientResponseDTO(
                saved.getId(),
                saved.getBloodType(),
                saved.getCity(),
                saved.getUser().getId()
        );
    }

    // get all recipients
    public List<RecipientResponseDTO> getAllRecipients() {
        return recipientRepository.findAll()
                .stream()
                .map(d -> new RecipientResponseDTO(
                        d.getId(),
                        d.getBloodType(),
                        d.getCity(),
                        d.getUser().getId()
                ))
                .collect(Collectors.toList());
    }

    // get recipient by ID
    public RecipientResponseDTO getRecipientById(Long id) {
        Recipient recipient = recipientRepository.findById(id)
                .orElseThrow(() -> new RecipientNotFoundException("Recipient not found with id: " + id));

        return new RecipientResponseDTO(
                recipient.getId(),
                recipient.getBloodType(),
                recipient.getCity(),
                recipient.getUser().getId()
        );
    }

    // delete recipient by ID
    public void deleteRecipient(Long id) {
        if (!recipientRepository.existsById(id)) {
            throw new RecipientNotFoundException("Recipient not found with id: " + id);
        }
        recipientRepository.deleteById(id);
    }
}

