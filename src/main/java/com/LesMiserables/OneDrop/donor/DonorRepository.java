package com.LesMiserables.OneDrop.donor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    Donor findByEmail(String email);
}
