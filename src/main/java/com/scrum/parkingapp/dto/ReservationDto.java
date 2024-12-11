package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.dto.validation.ValidReservDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReservationDto {

    private Long id;//

    private UserDto driver;//

    private Double price;

    private ParkingSpotDto parkingSpot;//

    private LicensePlateDto licensePlate;//

    @ValidReservDate
    @NotNull
    private LocalDateTime startDate;

    @ValidReservDate
    @NotNull
    private LocalDateTime endDate;


}
