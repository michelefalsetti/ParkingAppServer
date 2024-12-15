package com.scrum.parkingapp.utils;

import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DatesGetter {

    @MockBean
    private JwtService jwtService; // Mock del servizio richiesto

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RevokedTokenService revokedTokenService;

    @MockBean
    private LoggedUserDetailsService loggedUserDetailsService;


    @WithMockCustomUser
    public ReservationDto getReservationDto(Authentication authentication) {
        LoggedUserDetails loggedUser = null;
        if (authentication != null) {
            loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        }


        ParkingSpotIdDto parkingSpotIdDto = new ParkingSpotIdDto();
        parkingSpotIdDto.setId(1L);
        LocalDateTime startDate = LocalDateTime.of(2021, 12, 1, 17, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 2, 19, 30);

        UserIdDto driver = new UserIdDto();
        driver.setUserId( loggedUser != null ? loggedUser.getId() : UUID.randomUUID());
        LicensePlateIdDto licensePlateIdDto = new LicensePlateIdDto();
        licensePlateIdDto.setId(1L);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setParkingSpotId(1L);
        reservationDto.setUser(getUserDto("DRIVER"));
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setPrice(10.0);
        reservationDto.setLicensePlateId(1L);

        return reservationDto;
    }

    public  LicensePlateDto getLicensePlateDto() {
        LicensePlateDto licensePlateDto = new LicensePlateDto();
        licensePlateDto.setId(1L);
        licensePlateDto.setLpNumber("AB123CD");
        return licensePlateDto;
    }

    public UserDto getUserDto(String role) {
        LocalDateTime date = LocalDateTime.of(2006, 12, 1, 17, 0);
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setFirstName("Mario");
        userDto.setLastName("Rossi");
        userDto.setBirthDate(LocalDate.from(date));
        userDto.setRole(role);

        return userDto;
    }
    public UserDto getUserDto(UUID id, String FirstName, String LastName, String role) {
        LocalDateTime date = LocalDateTime.of(2006, 12, 1, 17, 0);
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName(FirstName);
        userDto.setLastName(LastName);
        userDto.setBirthDate(LocalDate.from(date));
        userDto.setRole(role);

        return userDto;
    }


    @WithMockCustomUser
    public  ParkingSpotDto getParkingSpotDto(Authentication authentication) {
        LoggedUserDetails loggedUser = null;
        if (authentication != null) {
            loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(1L);
        ParkingSpaceIdDto parkingSpaceId = new ParkingSpaceIdDto();
        parkingSpaceId.setId(parkingSpaceDto.getId());

        parkingSpotDto.setParkingSpaceId(1L);
        parkingSpotDto.setNumber("TEST-1");
        parkingSpotDto.setReservations(null);

        return parkingSpotDto;

    }

    @WithMockCustomUser(role = "OWNER")
    public ParkingSpaceDto getParkingSpaceDto(Authentication authentication) {
        LoggedUserDetails loggedUser = null;
        if (authentication != null) {
            loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setId(1L);
        parkingSpaceDto.setName("Test Parking");
        parkingSpaceDto.setAddress("123 Test Street");
        parkingSpaceDto.setCity("Test City");

        UserIdDto owner = new UserIdDto();
        owner.setUserId(loggedUser != null ? loggedUser.getId() : UUID.randomUUID());
        parkingSpaceDto.setUserId(owner);
        return parkingSpaceDto;
    }


}
