package com.scrum.parkingapp.data.service.implem;


import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.Reservation;
import com.scrum.parkingapp.data.service.ParkingSpotService;
import com.scrum.parkingapp.dto.ParkingSpotDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotDao parkingSpotDao;
    private final ParkingSpaceDao parkingSpaceDao;
    private final ReservationDao reservationDao;
    private final ModelMapper modelMapper;


    @Override
    public ParkingSpotDto getById(Long id) {
        return parkingSpotDao.findById(id)
                .map(parkingSpot -> modelMapper.map(parkingSpot, ParkingSpotDto.class))
                .orElse(null);
    }


    @Override
    @Transactional
    public ParkingSpotDto save(ParkingSpotDto parkingSpotDto) {
        ParkingSpot parkingSpot = modelMapper.map(parkingSpotDto, ParkingSpot.class);

        System.out.println("Post Mapping: " + parkingSpot);

        ParkingSpot ps = parkingSpotDao.save(parkingSpot);
        System.out.println("Post Save: " + ps);
        return modelMapper.map(ps, ParkingSpotDto.class);
    }

    @Override
    public List<ParkingSpotDto> getByParkingSpaceId(Long parkingSpaceId) {
        return parkingSpotDao.findAllByParkingspaceId(parkingSpaceId)
                .stream()
                .map(parkingSpot -> modelMapper.map(parkingSpot, ParkingSpotDto.class))
                .toList();

    }

    @Override
    public Boolean delete(Long id) {
        ParkingSpot parkingSpot = parkingSpotDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("ParkingSpot not found with id: " + id)
        );

        List<Reservation> reservations = parkingSpot.getReservations();
        if (reservations != null && !reservations.isEmpty()) {
            System.out.println("Deleting reservations");
            parkingSpot.setReservations(null);
            reservationDao.deleteAll(reservations);
            reservationDao.flush();
            System.out.println("Reservations deleted");

        }

        Long spaceId = parkingSpot.getParkingspaceId().getId();
        ParkingSpace parkingSpace = parkingSpaceDao.findById(spaceId).orElseThrow(
                () -> new IllegalArgumentException("ParkingSpace not found with id: " + spaceId)
        );
        parkingSpace.getParkingSpots().remove(parkingSpot);
        parkingSpaceDao.save(parkingSpace);
        System.out.println("ParkingSpot removed from ParkingSpace");

        parkingSpotDao.delete(parkingSpot);
        parkingSpotDao.flush();
        System.out.println("ParkingSpot deleted");
        return true;

    }


}
