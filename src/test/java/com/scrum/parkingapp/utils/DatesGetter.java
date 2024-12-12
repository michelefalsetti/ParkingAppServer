package com.scrum.parkingapp.utils;

import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class DatesGetter {

    public static ReservationDto getReservationDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();


        ParkingSpotIdDto parkingSpotIdDto = new ParkingSpotIdDto();
        parkingSpotIdDto.setId(1L);
        LocalDateTime startDate = LocalDateTime.of(2021, 12, 1, 17, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 2, 19, 30);

        UserIdDto driver = new UserIdDto();
        driver.setUserId(loggedUser.getId());
        LicensePlateIdDto licensePlateIdDto = new LicensePlateIdDto();
        licensePlateIdDto.setId(1L);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setParkingSpot(getParkingSpotDto());
        reservationDto.setDriver(getUserDto("DRIVER"));
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setPrice(10.0);
        reservationDto.setLicensePlate(getLicensePlateDto());

        return reservationDto;
    }

    public static LicensePlateDto getLicensePlateDto() {
        LicensePlateDto licensePlateDto = new LicensePlateDto();
        licensePlateDto.setId(1L);
        licensePlateDto.setLpNumber("AB123CD");
        return licensePlateDto;
    }

    public static UserDto getUserDto(String role) {
        LocalDateTime date = LocalDateTime.of(2006, 12, 1, 17, 0);
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setFirstName("Mario");
        userDto.setLastName("Rossi");
        userDto.setBirthDate(LocalDate.from(date));
        userDto.setRole(role);

        return userDto;
    }

    public static ParkingSpotDto getParkingSpotDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(1L);
        ParkingSpaceIdDto parkingSpaceId = new ParkingSpaceIdDto();
        parkingSpaceId.setId(parkingSpaceDto.getId());

        parkingSpotDto.setParkingSpaceId(parkingSpaceId);
        parkingSpotDto.setNumber("TEST-1");
        parkingSpotDto.setReservations(null);

        return parkingSpotDto;

    }

    public static ParkingSpaceDto getParkingSpaceDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setId(1L);
        parkingSpaceDto.setName("Test Parking");
        parkingSpaceDto.setAddress("123 Test Street");
        parkingSpaceDto.setCity("Test City");

        UserIdDto owner = new UserIdDto();
        owner.setUserId(loggedUser.getId());
        parkingSpaceDto.setOwner(owner);
        return parkingSpaceDto;
    }


}
