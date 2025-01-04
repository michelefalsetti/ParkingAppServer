package com.scrum.parkingapp.controller;


import com.scrum.parkingapp.data.entities.Address;
import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.dto.AddressDto;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/api/v1/parkingSpaces")// produces indica che
@CrossOrigin(origins = "*", allowedHeaders = "*") // indica
@RequiredArgsConstructor
@Slf4j // indica che il logger è di tipo log4j
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;


    @GetMapping(path= "/getAll")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ParkingSpaceDto>> getAll() {
        List<ParkingSpaceDto> parkingSpacesDto = parkingSpaceService.getAllDto();
        if (parkingSpacesDto == null || parkingSpacesDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(parkingSpacesDto, HttpStatus.OK);
    }

    @GetMapping(path= "/getById/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ParkingSpaceDto> getById(@PathVariable Long id) {
        ParkingSpaceDto parkingSpaceDto = parkingSpaceService.getById(id);
        if (parkingSpaceDto == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(parkingSpaceDto, HttpStatus.OK);
    }

    @GetMapping(path= "/getBySearch/{city}/{startDate}/{endDate}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ParkingSpaceDto>> getBySearch(@PathVariable String city,@PathVariable
    String startDate,@PathVariable String endDate) {

        LocalDateTime parsedStartDate = LocalDateTime.parse(startDate);
        LocalDateTime parsedEndDate = LocalDateTime.parse(endDate);

        List<ParkingSpaceDto> parkingSpacesDto = parkingSpaceService.findAllByCityAndStartDateAndEndDate(city, parsedStartDate, parsedEndDate);
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
    @PreAuthorize("#parkingSpaceDto.getUserId().getUserId() == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<ParkingSpaceDto> addParkingSpace(@Valid @RequestBody ParkingSpaceDto parkingSpaceDto) {
        System.out.println("pre add");
        ParkingSpaceDto psDto = parkingSpaceService.save(parkingSpaceDto);
        System.out.println("post add");
        if (psDto == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(psDto, HttpStatus.OK);
    }
    /*
    @GetMapping(path= "/delete/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<ParkingSpaceDto> deleteParkingSpace(@PathVariable Long id) {
        ParkingSpaceDto psDto = parkingSpaceService.deleteById(id);
        if (psDto == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(psDto, HttpStatus.OK);
    }*/

    @GetMapping(path= "/getAllAddresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addressesDto = parkingSpaceService.getAllAddresses();
        if (addressesDto == null || addressesDto.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(addressesDto, HttpStatus.OK);
    }



}
