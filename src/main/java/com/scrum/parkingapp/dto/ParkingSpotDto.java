package com.scrum.parkingapp.dto;

import com.scrum.parkingapp.data.domain.SpotType;
import com.scrum.parkingapp.data.entities.Reservation;
import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkingSpotDto {

    private Long id;

    private String number;

    private SpotType type;

    private Long parkingSpaceId;

    private List<ReservationDto> reservations = new ArrayList<>();

    private Double basePrice;
}
