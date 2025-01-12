package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.data.domain.SpotType;
import com.scrum.parkingapp.data.entities.Reservation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReservationWithDetailsDto extends ReservationDto {

    private String spaceName;
    private String spotNumber;
    private SpotType spotType;

    // Constructor per mappare i risultati della query
    public ReservationWithDetailsDto() {
    }
}