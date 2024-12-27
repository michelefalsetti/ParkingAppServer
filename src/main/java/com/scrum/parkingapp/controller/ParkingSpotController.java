package com.scrum.parkingapp.controller;


import com.scrum.parkingapp.data.service.ParkingSpotService;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.dto.ParkingSpotDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/parkingSpots")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;


    @GetMapping(path= "/getBySpaceId/{idSpace}/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSpotDto>> getByParkingSpaceId(@PathVariable Long idSpace, @PathVariable UUID idUser) {
        List<ParkingSpotDto> parkingSpotsDto = parkingSpotService.getByParkingSpaceId(idSpace);
        if (parkingSpotsDto == null || parkingSpotsDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(parkingSpotsDto, HttpStatus.OK);
    }


    @PostMapping(path= "/add/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<ParkingSpotDto> addParkingSpot(@RequestBody ParkingSpotDto parkingSpot,
                                                         @PathVariable UUID idUser) {
        System.out.println("pre add");
        ParkingSpotDto parkingSpotDto = parkingSpotService.save(parkingSpot);
        System.out.println("post add");
        if (parkingSpotDto == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(parkingSpotDto, HttpStatus.OK);
    }




}
