package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.data.entities.LicensePlate;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.dto.validation.ValidReservDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReservationDto {

    private Long id;

    private Double price;

    private UserIdDto driver;

    private LicensePlateDtoId licensePlate;

    private ParkingSpotIdDto parkingSpot;

    @ValidReservDate
    @NotNull
    private LocalDateTime startDate;

    @ValidReservDate
    @NotNull
    private LocalDateTime endDate;


}
