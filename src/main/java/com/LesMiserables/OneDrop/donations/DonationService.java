package com.LesMiserables.OneDrop.donations;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.donor.DonorRepository;
import com.LesMiserables.OneDrop.recipient.Recipient;
import com.LesMiserables.OneDrop.recipient.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepo donationRepo;
    private final DonorRepository donorRepository;
    private final RecipientRepository recipientRepository;

    public Optional<DonationAppointment> getUpcomingAppointment(Long donorId) {
        return donationRepo.findTopByDonorIdAndStatusNotOrderByAppointmentDateAsc(
                donorId,
                DonationAppointment.Status.CANCELLED
        );
    }

    public DonationAppointment bookAppointment(Long donorId, Long recipientId, String hospital, LocalDate date) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        // Check 90-day eligibility rule
        if (!donor.isEligibleToDonate()) {
            throw new IllegalStateException("Donor is not eligible to donate yet. Must wait 90 days since last donation.");
        }

        // Check if donor already has an active appointment
        Optional<DonationAppointment> existing = getUpcomingAppointment(donorId);
        if (existing.isPresent()) {
            throw new IllegalStateException("Donor already has an active appointment!");
        }

        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        DonationAppointment appointment = DonationAppointment.builder()
                .donor(donor)
                .recipient(recipient)
                .hospital(hospital)
                .appointmentDate(date)
                .status(DonationAppointment.Status.PENDING)
                .build();

        return donationRepo.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        DonationAppointment appointment = donationRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        appointment.setStatus(DonationAppointment.Status.CANCELLED);
        donationRepo.save(appointment);
    }

    public void completeAppointment(Long appointmentId) {
        DonationAppointment appointment = donationRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found!"));

        appointment.setStatus(DonationAppointment.Status.COMPLETED);

        // Update donor's last donation date
        Donor donor = appointment.getDonor();
        donor.setLastDonationDate(LocalDate.now());

        donationRepo.save(appointment);
        donorRepository.save(donor);
    }
}
