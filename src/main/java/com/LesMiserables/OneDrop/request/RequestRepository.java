package com.LesMiserables.OneDrop.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRecipient_Id(Long recipientId);

    List<Request> findByMatchedDonor_Id(Long donorId);

    List<Request> findByStatus(Request.Status status);

    List<Request> findByMatchedDonor_IdAndStatus(Long donorId, Request.Status status);

    List<Request> findByStatusInAndRequiredByBefore(List<Request.Status> statuses, LocalDateTime time);
}
