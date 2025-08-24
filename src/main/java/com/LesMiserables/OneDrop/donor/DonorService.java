package com.LesMiserables.OneDrop.donor;

import com.LesMiserables.OneDrop.authentication.User;
import com.LesMiserables.OneDrop.authentication.UserRepository;
import com.LesMiserables.OneDrop.donor.dto.DonorRequestDTO;
import com.LesMiserables.OneDrop.donor.dto.DonorResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final UserRepository userRepository;

    public DonorService(DonorRepository donorRepository, UserRepository userRepository) {
        this.donorRepository = donorRepository;
        this.userRepository = userRepository;
    }

    // Add a new donor
    public DonorResponseDTO addDonor(DonorRequestDTO request) {
        // Fetch the User entity from DB using userId from DTO
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Donor donor = Donor.builder()
                .bloodType(request.getBloodType())
                .city(request.getCity())
                .user(user)
                .build();

        Donor saved = donorRepository.save(donor);

        return new DonorResponseDTO(
                saved.getId(),
                saved.getBloodType(),
                saved.getCity(),
                saved.getUser().getId()
        );
    }

    // Get all donors
    public List<DonorResponseDTO> getAllDonors() {
        return donorRepository.findAll()
                .stream()
                .map(d -> new DonorResponseDTO(
                        d.getId(),
                        d.getBloodType(),
                        d.getCity(),
                        d.getUser().getId()
                ))
                .collect(Collectors.toList());
    }

    // Get donor by ID
    public DonorResponseDTO getDonorById(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));

        return new DonorResponseDTO(
                donor.getId(),
                donor.getBloodType(),
                donor.getCity(),
                donor.getUser().getId()
        );
    }

    // Delete donor by ID
    public void deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new RuntimeException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
    }
}
