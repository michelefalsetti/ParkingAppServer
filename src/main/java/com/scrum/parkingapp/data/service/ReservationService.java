package com.scrum.parkingapp.data.service;

import com.scrum.parkingapp.dto.ReservationDto;
import com.scrum.parkingapp.dto.ReservationWithDetailsDto;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    List<ReservationDto> getAllByUserId(UUID userId);

    List<ReservationDto> getAllBySpotId(Long spotId);

    List<ReservationDto> getAllReservation();

    ReservationDto save(ReservationDto reservationDto);

    ReservationDto deleteById(Long id);

    List<ReservationWithDetailsDto> getUserReservationsWithDetails(UUID userId);

}
