package com.LesMiserables.OneDrop.donations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<?> getUpcomingAppointment(@PathVariable Long donorId) {
        return donationService.getUpcomingAppointment(donorId)
                .map(appointment -> ResponseEntity.ok(Map.of(
                        "appointmentId", appointment.getId(),
                        "appointmentDate", appointment.getAppointmentDate(),
                        "status", appointment.getStatus(),
                        "hospital", appointment.getHospital(),
                        "recipientName", appointment.getRecipient().getUser().getFullName(),
                        "recipientBloodType", appointment.getRecipient().getBloodType(),
                        "recipientLocation", appointment.getRecipient().getLocation()
                )))
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/book")
    public ResponseEntity<DonationAppointment> bookAppointment(@RequestBody Map<String, String> payload) {
        Long donorId = Long.valueOf(payload.get("donorId"));
        Long recipientId = Long.valueOf(payload.get("recipientId"));
        String hospital = payload.get("hospital");
        LocalDate date = LocalDate.parse(payload.get("appointmentDate"));

        return ResponseEntity.ok(
                donationService.bookAppointment(donorId, recipientId, hospital, date)
        );
    }

    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        donationService.cancelAppointment(appointmentId);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    @PutMapping("/complete/{appointmentId}")
    public ResponseEntity<String> completeAppointment(@PathVariable Long appointmentId) {
        donationService.completeAppointment(appointmentId);
        return ResponseEntity.ok("Appointment completed successfully");
    }
}
