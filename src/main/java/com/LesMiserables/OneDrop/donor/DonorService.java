package com.LesMiserables.OneDrop.donor;

import com.LesMiserables.OneDrop.donor.dto.DonorLocationDTO;
import com.LesMiserables.OneDrop.exceptions.DonorNotFoundException;
import com.LesMiserables.OneDrop.user.User;
import com.LesMiserables.OneDrop.user.UserRepository;
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

    // add new donor
    public DonorResponseDTO addDonor(DonorRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Donor donor = Donor.builder()
                .bloodType(request.getBloodType())
                .user(user)
                .build();

        Donor saved = donorRepository.save(donor);

        return new DonorResponseDTO(
                saved.getId(),
                saved.getBloodType(),
                saved.isEligibleToDonate(),
                saved.getUser().getId(),
                saved.getLocation()
        );
    }

    // auto update current location
    public DonorResponseDTO updateDonorLocation(Long donorId, DonorLocationDTO location) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found"));

        donor.setLocation(location.getLocation());
        donorRepository.save(donor);

        return new DonorResponseDTO(
                donor.getId(),
                donor.getBloodType(),
                donor.isEligibleToDonate(),
                donor.getUser().getId(),
                donor.getLocation()
        );
    }

    // get all donors
    public List<DonorResponseDTO> getAllDonors() {
        return donorRepository.findAll()
                .stream()
                .map(d -> new DonorResponseDTO(
                        d.getId(),
                        d.getBloodType(),
                        d.isEligibleToDonate(),
                        d.getUser().getId(),
                        d.getLocation()
                ))
                .collect(Collectors.toList());
    }

    // get donor by ID
    public DonorResponseDTO getDonorById(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));

        return new DonorResponseDTO(
                donor.getId(),
                donor.getBloodType(),
                donor.isEligibleToDonate(),
                donor.getUser().getId(),
                donor.getLocation()
        );
    }

    // delete donor by ID
    public void deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new DonorNotFoundException("Donor not found with id: " + id);
        }
        donorRepository.deleteById(id);
    }
}
