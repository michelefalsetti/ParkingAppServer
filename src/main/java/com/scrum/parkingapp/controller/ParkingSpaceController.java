package com.scrum.parkingapp.controller;


import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
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
@RequestMapping(value = "/api/v1/parkingSpaces")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;


    @GetMapping(path= "/getAll")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSpaceDto>> getAll() {
        List<ParkingSpaceDto> parkingSpacesDto = parkingSpaceService.getAllDto();
        if (parkingSpacesDto == null || parkingSpacesDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(parkingSpacesDto, HttpStatus.OK);
    }


    @GetMapping(path= "/getByUser/{idUser}")
    @PreAuthorize("#idUser == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSpaceDto>> getByUser(@PathVariable UUID idUser) {
        List<ParkingSpaceDto> parkingSpacesDto = parkingSpaceService.getAllByOwnerId(idUser);

        if (parkingSpacesDto == null || parkingSpacesDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(parkingSpacesDto, HttpStatus.OK);
    }

    @PostMapping(path= "/add")
    @PreAuthorize("#parkingSpaceDto.getOwner().getUserId() == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> addParkingSpace(@Valid @RequestBody ParkingSpaceDto parkingSpaceDto) {
        System.out.println("pre add");
        ParkingSpaceDto psDto = parkingSpaceService.save(parkingSpaceDto);
        System.out.println("post add");
        if (psDto == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }



}
