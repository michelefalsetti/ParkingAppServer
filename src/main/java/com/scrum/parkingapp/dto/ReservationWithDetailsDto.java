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
        /*
        Reservation r = (Reservation) queryResult[0];
        this.setId(r.getId());
        this.setStartDate(r.getStartDate());
        this.setEndDate(r.getEndDate());
        this.setPrice(r.getPrice());
        this.setLicensePlate(r.getLicensePlate());
        this.setPaymentMethod(r.getPaymentMethod());
        this.setParkingSpotId(r.getParkingSpot().getId());
        this.spaceName = (String) queryResult[1];
        this.spotType = SpotType.values()[(Integer) queryResult[2]];*/
    }
}