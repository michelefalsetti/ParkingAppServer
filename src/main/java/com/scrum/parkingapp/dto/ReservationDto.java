package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.dto.validation.ValidReservDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReservationDto {

    private Long id;//

    private UserDto user;//

    private Double price;
    private Long parkingSpotId;//

    private Long licensePlateId;//

    @ValidReservDate
    @NotNull
    private LocalDateTime startDate;

    @ValidReservDate
    @NotNull
    private LocalDateTime endDate;


}
