package com.scrum.parkingapp.data.service;

import com.scrum.parkingapp.dto.ParkingSpaceDto;
import java.util.List;
import java.util.UUID;


public interface ParkingSpaceService {

    ParkingSpaceDto getById(Long id);

    ParkingSpaceDto save(ParkingSpaceDto parkingSpaceDto);

    List<ParkingSpaceDto> getAllDto();

    List<ParkingSpaceDto> getAllByOwnerId(UUID ownerId);




}
