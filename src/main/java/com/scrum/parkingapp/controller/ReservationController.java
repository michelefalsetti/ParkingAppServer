package com.scrum.parkingapp.controller;

import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.data.service.ReservationService;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.dto.ReservationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/reservations")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping(path= "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDto>> getAll() {
        List<ReservationDto> reservationsDto = reservationService.getAllReservation();
        if (reservationsDto == null || reservationsDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(reservationsDto, HttpStatus.OK);
    }

    @GetMapping(path= "/getByUser/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDto>> getByUser(@PathVariable UUID idUser) {
        List<ReservationDto> reservationsDto = reservationService.getAllByUserId(idUser);

        if (reservationsDto == null || reservationsDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(reservationsDto, HttpStatus.OK);
    }

    @GetMapping(path= "/getBySpot/{idSpot}")
    //@PreAuthorize()
    public ResponseEntity<List<ReservationDto>> getBySpot(@PathVariable Long idSpot) {
        List<ReservationDto> reservationsDto = reservationService.getAllBySpotId(idSpot);

        if (reservationsDto == null || reservationsDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(reservationsDto, HttpStatus.OK);
    }

    @PutMapping(path = "/add/{idUser}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationDto> addReservation(@PathVariable UUID idUser,  @RequestBody ReservationDto reservationDto) {
        log.info("User ID from path: {}", idUser);
        log.info("DTO received: {}", reservationDto);
        System.out.println("Res dto: " + reservationDto.toString());

        if (!idUser.equals(reservationDto.getUser().getId())) {
            log.error("Mismatch between path user ID and body user ID");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            ReservationDto rDto = reservationService.save(reservationDto);
            log.info("Reservation saved: {}", rDto);
            return new ResponseEntity<>(rDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during reservation processing", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/delete/{idRes}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteReservation(@PathVariable Long idRes, @PathVariable UUID idUser) {
        ReservationDto r = reservationService.deleteById(idRes);
        if (r == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }





}
