package com.scrum.parkingapp.data.service;

import com.scrum.parkingapp.dto.ParkingSpotDto;
import java.util.List;
import java.util.UUID;


public interface ParkingSpotService {

    ParkingSpotDto getById(Long id);

    ParkingSpotDto save(ParkingSpotDto parkingSpotDto);

    List<ParkingSpotDto> getByParkingSpaceId(Long parkingSpaceId);
}
