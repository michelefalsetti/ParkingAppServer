package com.scrum.parkingapp.controller;

import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.data.service.ReservationService;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.dto.ReservationDto;
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
    //@PreAuthorize("hasRole('ADMIN')")
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

    @PostMapping(path= "/add/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> addReservation(@PathVariable UUID idUser, @RequestBody ReservationDto reservationDto) {
        System.out.println("pre add reservation");
        ReservationDto rDto = reservationService.save(reservationDto);
        if (rDto == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }




}
