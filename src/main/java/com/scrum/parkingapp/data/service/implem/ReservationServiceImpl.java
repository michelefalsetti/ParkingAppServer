package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.entities.Reservation;
import com.scrum.parkingapp.data.service.ReservationService;
import com.scrum.parkingapp.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final ParkingSpotDao parkingSpotDao;
    private final ModelMapper modelMapper;


    @Override
    public List<ReservationDto> getAllReservation() {
        return reservationDao.findAllReservation().stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public ReservationDto save(ReservationDto reservationDto) {
        System.out.println("ReservationDto: " + reservationDto);
        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);

        Reservation r = reservationDao.save(reservation);
        return modelMapper.map(r, ReservationDto.class);
    }

    @Override
    public ReservationDto deleteById(Long id) {
        Reservation r = reservationDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid reservation ID"));

        if (r != null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        reservationDao.deleteById(id);
        return modelMapper.map(r, ReservationDto.class);

    }


    @Override
    public List<ReservationDto> getAllByUserId(UUID userId) {
       return reservationDao.findAllByUserId(userId).stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                .collect(java.util.stream.Collectors.toList());

    }

    @Override
    public List<ReservationDto> getAllBySpotId(Long spotId) {
        return reservationDao.findAllByParkingSpotId(spotId).stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                .collect(java.util.stream.Collectors.toList());
    }


}
