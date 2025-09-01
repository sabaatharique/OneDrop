package com.LesMiserables.OneDrop.donations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationRepo extends JpaRepository<DonationAppointment, Long> {

    // Check if donor already has an active appointment
    Optional<DonationAppointment> findTopByDonorIdAndStatusNotOrderByAppointmentDateAsc(
            Long donorId,
            DonationAppointment.Status status
    );
}
