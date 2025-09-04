package com.LesMiserables.OneDrop.donor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLastDonationDTO {
    private LocalDate lastDonationDate;
}
