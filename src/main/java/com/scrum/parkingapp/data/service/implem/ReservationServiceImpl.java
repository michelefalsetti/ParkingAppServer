package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.Reservation;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.ReservationService;
import com.scrum.parkingapp.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private final UsersDao usersDao;
    private final ParkingSpotDao parkingSpotDao;
    private final ModelMapper modelMapper;


    @Override
    public List<ReservationDto> getAllReservation() {
        return reservationDao.findAllReservation().stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public ReservationDto save(ReservationDto reservationDto) {
        log.info("ReservationServiceImpl", "save", "reservationDto: " + reservationDto);

        // Recupera l'User associato
        User user = usersDao.findById(reservationDto.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid user ID"));

        // Rimuovi la vecchia Reservation
        if (user.getReservations() == null) {
            user.setReservations(new ArrayList<>());
            usersDao.save(user);
        }

        // Trova la LicensePlate associata
        // Trova il ParkingSpot associato
        ParkingSpot parkingSpot = parkingSpotDao.findById(reservationDto.getParkingSpotId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid parking spot ID"));

        // Crea la nuova Reservation
        Reservation reservation = new Reservation();
        reservation.setLicensePlate(reservationDto.getLicensePlate());
        reservation.setUser(user);
        reservation.setParkingSpot(parkingSpot);
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setPrice(reservationDto.getPrice());
        reservation.setPaymentMethod(reservationDto.getPaymentMethod());

        // Validazioni aggiuntive
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Aggiorna la disponibilità del ParkingSpot
        parkingSpot.getReservations().add(reservation);

        // Salva la nuova Reservation
        Reservation savedReservation = reservationDao.save(reservation);

        // Aggiorna la relazione bidirezionale con l'utente
        user.getReservations().add(savedReservation);
        usersDao.save(user);

        // Salva l'aggiornamento del ParkingSpot
        parkingSpotDao.save(parkingSpot);

        // Restituisci il DTO
        return modelMapper.map(savedReservation, ReservationDto.class);
    }


    @Override
    public ReservationDto deleteById(Long id) {
        Reservation r = reservationDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid reservation ID"));

        if (r == null) {
            throw new IllegalArgumentException("Reservation not found");
        }

        reservationDao.deleteParkingSpotReservationsByReservationId(id);
        System.out.println("Relation deleted");
        reservationDao.deleteById(id);
        System.out.println("Reservation deleted");
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
