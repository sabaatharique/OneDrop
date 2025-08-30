package com.LesMiserables.OneDrop.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRecipientId(Long recipientId);
    List<Request> findByStatus(Request.Status status);
}