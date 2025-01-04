package com.scrum.parkingapp.utils;

import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.data.entities.Address;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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

    @MockBean
    private ModelMapper modelMapper;


    @WithMockCustomUser
    public ReservationDto getReservationDto(Authentication authentication) {
        LoggedUserDetails loggedUser = null;
        if (authentication != null) {
            loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        }


        ParkingSpotIdDto parkingSpotIdDto = new ParkingSpotIdDto();
        parkingSpotIdDto.setId(1L);
        String strStartDate = "2025-12-01T17:00";
        String strEndDate =   "2025-12-02T19:30";
        LocalDateTime startDate =  LocalDateTime.parse(strStartDate);
        LocalDateTime endDate = LocalDateTime.parse(strEndDate);

        UserIdDto driver = new UserIdDto();
        driver.setUserId( loggedUser != null ? loggedUser.getId() : UUID.randomUUID());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setParkingSpotId(1L);


        UserDto userDto = new UserDto();
        userDto.setId(loggedUser != null ? loggedUser.getId() : UUID.randomUUID());
        userDto.setFirstName("Mario");
        userDto.setLastName("Rossi");
        userDto.setBirthDate(LocalDate.of(2006, 12, 1));
        userDto.setRole("DRIVER");

        assert authentication != null;
        reservationDto.setUser(userDto);
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setPrice(10.0);
        reservationDto.setLicensePlate("AB123CD");

        return reservationDto;
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

        AddressDto  addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setLatitude(39.123456);
        addressDto.setLongitude(16.123456);
        addressDto.setStreet("123 Test Street");

        parkingSpaceDto.setId(1L);
        parkingSpaceDto.setName("Test Parking");
        parkingSpaceDto.setAddress(addressDto);

        UserIdDto owner = new UserIdDto();
        owner.setUserId(loggedUser != null ? loggedUser.getId() : UUID.randomUUID());
        parkingSpaceDto.setUserId(owner);
        return parkingSpaceDto;
    }




    @WithMockCustomUser
    public ParkingSpace getParkingSpace(Authentication authentication) {
        LoggedUserDetails loggedUser = null;
        if (authentication != null) {
            loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        }

        ParkingSpace parkingSpace = new ParkingSpace();

        Address address = new Address();

        address.setStreet("123 Test Street");
        address.setCity("City1");
        address.setLatitude(0.0);
        address.setLongitude(0.0);

        parkingSpace.setName("Test Parking");
        parkingSpace.setAddress(address);
        parkingSpace.setId(1L);


        User user = new User();
        user.setId(loggedUser != null ? loggedUser.getId() : UUID.randomUUID());

        parkingSpace.setUser( user);

        return parkingSpace;
    }

    @WithMockCustomUser
    public ParkingSpot getParkingSpot(Authentication authentication, Long id) {
        LoggedUserDetails loggedUser = null;
        if (authentication != null) {
            loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        }

        ParkingSpot parkingSpot = new ParkingSpot();

        parkingSpot.setId(id);
        parkingSpot.setNumber("TEST-1");
        parkingSpot.setBasePrice(10.0);


        return parkingSpot;
    }


}
