package com.scrum.parkingapp.data.service;

import com.scrum.parkingapp.data.entities.Address;
import com.scrum.parkingapp.dto.AddressDto;
import com.scrum.parkingapp.dto.ParkingSpaceDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ParkingSpaceService {

    ParkingSpaceDto getById(Long id);

    ParkingSpaceDto save(ParkingSpaceDto parkingSpaceDto);

    List<ParkingSpaceDto> getAllDto();

    List<ParkingSpaceDto> findAllByCityAndStartDateAndEndDate(String city, LocalDateTime startDate, LocalDateTime endDate);

    List<ParkingSpaceDto> getAllByOwnerId(UUID ownerId);

    List<AddressDto> getAllAddresses();


}