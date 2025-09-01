package com.LesMiserables.OneDrop.match;

import com.LesMiserables.OneDrop.donor.Donor;
import com.LesMiserables.OneDrop.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByRequest(Request request);
    List<Match> findByDonor(Donor donor);
    boolean existsByDonorAndRequest(Donor donor, Request request);
}