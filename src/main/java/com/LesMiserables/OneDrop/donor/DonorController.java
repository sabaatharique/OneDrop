package com.LesMiserables.OneDrop.donor;

import com.LesMiserables.OneDrop.donor.dto.DonorRequestDTO;
import com.LesMiserables.OneDrop.donor.dto.DonorResponseDTO;
import com.LesMiserables.OneDrop.donor.dto.UpdateLastDonationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public ResponseEntity<DonorResponseDTO> addDonor(@RequestBody DonorRequestDTO request) {
        DonorResponseDTO response = donorService.addDonor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DonorResponseDTO>> getAllDonors() {
        List<DonorResponseDTO> donors = donorService.getAllDonors();
        return new ResponseEntity<>(donors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorResponseDTO> getDonorById(@PathVariable Long id) {
        DonorResponseDTO donor = donorService.getDonorById(id);
        return new ResponseEntity<>(donor, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DonorResponseDTO> getDonorByUserId(@PathVariable Long userId) {
        DonorResponseDTO donor = donorService.getDonorByUserId(userId);
        return new ResponseEntity<>(donor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonor(@PathVariable Long id) {
        donorService.deleteDonor(id);
        return new ResponseEntity<>("Donor deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{userId}/last-donation")
    public ResponseEntity<DonorResponseDTO> updateLastDonationDate(
            @PathVariable Long userId, 
            @RequestBody UpdateLastDonationDTO request) {
        DonorResponseDTO response = donorService.updateLastDonationDate(userId, request.getLastDonationDate());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}