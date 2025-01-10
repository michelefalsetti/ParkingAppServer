package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.data.domain.PaymentMethodType;
import com.scrum.parkingapp.dto.validation.ValidReservationDate;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {

    private Long id;

    @NotNull(message = "User is required")
    private UserDto user;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @NotNull(message = "Payment method is required")
    private PaymentMethodType paymentMethod;

    @NotNull(message = "ParkingSpot ID is required")
    private Long parkingSpotId;

    @NotNull(message = "LicensePlate is required")
    private String licensePlate;

    @ValidReservationDate //ho appena messo il valid, se fallisce l'add e' per colpa sua
    private LocalDateTime startDate;

    @ValidReservationDate
    private LocalDateTime endDate;
}
