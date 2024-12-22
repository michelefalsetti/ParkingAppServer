package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.data.dao.LicensePlateDao;
import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.LicensePlate;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDao reservationDao;
    private  final LicensePlateDao licensePlateDao;
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
        System.out.println("ReservationDto: " + reservationDto);

        // Recupera l'User associato
        User user = usersDao.findById(reservationDto.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid user ID"));

        // Se l'utente ha una vecchia Reservation, rimuovila
        if (user.getReservation() != null) {
            Reservation oldReservation = user.getReservation();
            ParkingSpot oldParkingSpot = oldReservation.getParkingSpot();

            // Rimuovi la vecchia Reservation dalla lista nel ParkingSpot
            if (oldParkingSpot != null) {
                oldParkingSpot.getReservations().remove(oldReservation);
                parkingSpotDao.save(oldParkingSpot);
            }

            // Rimuovi la relazione bidirezionale
            user.setReservation(null);
            reservationDao.delete(oldReservation);
            reservationDao.flush();
        }

        // Trova la LicensePlate associata
        Long lpId = reservationDto.getLicensePlateId();
        LicensePlate licensePlate = licensePlateDao.findById(lpId).orElseThrow(
                () -> new IllegalArgumentException("Invalid license plate ID"));

        // Trova il ParkingSpot associato
        ParkingSpot parkingSpot = parkingSpotDao.findById(reservationDto.getParkingSpotId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid parking spot ID"));

        // Crea la nuova Reservation
        Reservation reservation = new Reservation();
        reservation.setLicensePlate(licensePlate);
        reservation.setUser(user);
        reservation.setParkingSpot(parkingSpot);
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setPrice(reservationDto.getPrice());

        // Validazioni aggiuntive
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Aggiorna la disponibilità del ParkingSpot
        parkingSpot.getReservations().add(reservation);
        if (! (reservation.getStartDate().isBefore(LocalDateTime.now()) &&
                                reservation.getEndDate().isAfter(LocalDateTime.now()))
        ) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Salva la nuova Reservation
        Reservation savedReservation = reservationDao.save(reservation);

        // Aggiorna la relazione bidirezionale con l'utente
        user.setReservation(savedReservation);
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
