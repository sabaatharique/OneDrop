package com.LesMiserables.OneDrop.request;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestExpiryService {

    private final RequestRepository requestRepo;

    // request exceeds required by time: PENDING/MATCHED -> EXPIRED
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void markExpiredRequests() {
        LocalDateTime now = LocalDateTime.now();
        List<Request> expiredRequests = requestRepo.findByStatusAndRequiredByBefore(Request.Status.PENDING, now);
        expiredRequests.forEach(r -> r.setStatus(Request.Status.EXPIRED));

        requestRepo.saveAll(expiredRequests);
        // System.out.println("Marked " + expiredRequests.size() + " requests as EXPIRED");
    }
}