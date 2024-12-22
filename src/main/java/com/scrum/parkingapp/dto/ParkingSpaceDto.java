package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkingSpaceDto {

    private Long id;

    private UserIdDto userId; //cambiato da owner a userId

    private List<ParkingSpotDto> parkingSpots = new ArrayList<>();

    private String name;

    private AddressDto address;


}
