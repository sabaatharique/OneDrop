package com.LesMiserables.OneDrop.request;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestExpiryService {

    private final RequestRepository requestRepo;

    // request exceeds required by time: PENDING/MATCHED -> EXPIRED
    @Scheduled(fixedRate = 60 * 60 * 1000)
    @Transactional
    public void markExpiredRequests() {
        LocalDateTime now = LocalDateTime.now();
        List<Request.Status> statuses = Arrays.asList(Request.Status.PENDING, Request.Status.MATCHED);
        List<Request> expiredRequests = requestRepo.findByStatusInAndRequiredByBefore(statuses, now);
        expiredRequests.forEach(r -> r.setStatus(Request.Status.EXPIRED));

        requestRepo.saveAll(expiredRequests);
    }
}