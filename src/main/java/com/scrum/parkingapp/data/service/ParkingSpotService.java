package com.scrum.parkingapp.data.service;

import com.scrum.parkingapp.dto.ParkingSpotDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ParkingSpotService {

    ParkingSpotDto getById(Long id);

    ParkingSpotDto save(ParkingSpotDto parkingSpotDto);

    List<ParkingSpotDto> getByParkingSpaceId(Long parkingSpaceId);

    Boolean delete(Long id);
}
