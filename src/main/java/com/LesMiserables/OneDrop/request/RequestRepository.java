package com.LesMiserables.OneDrop.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRecipientId(Long recipientId);
    List<Request> findByDonorId(Long donorId);
    List<Request> findByStatus(Request.Status status);
    //List<Request> findByStatusAndRequiredByBefore(Request.Status status, LocalDateTime time);
    List<Request> findByMatchedDonorIdAndStatus(Long donorId, Request.Status status);
    List<Request> findByStatusInAndRequiredByBefore(List<Request.Status> statuses, LocalDateTime time);
}