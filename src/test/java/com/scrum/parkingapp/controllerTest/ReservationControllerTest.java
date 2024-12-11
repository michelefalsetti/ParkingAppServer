package com.scrum.parkingapp.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.config.ModelMapperConfig;
import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.config.security.SecurityConfig;
import com.scrum.parkingapp.controller.ReservationController;
import com.scrum.parkingapp.data.entities.Reservation;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.ReservationService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.*;
import com.scrum.parkingapp.utils.WithMockCustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReservationController.class) // Cambia qui
@AutoConfigureMockMvc
@Import({SecurityConfig.class, ModelMapperConfig.class})
@ActiveProfiles("test")
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService; // Cambia in ParkingSpotService

    @MockBean // Cambia in MockBean
    private ModelMapper modelMapper;

    @MockBean
    private JwtService jwtService; // Mock del servizio richiesto

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RevokedTokenService revokedTokenService;

    @MockBean
    private LoggedUserDetailsService loggedUserDetailsService;


    @Autowired
    private ObjectMapper objectMapper; // Usa l'ObjectMapper di Spring

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "DRIVER")
    void testAddReservation_Driver() throws Exception {
        ReservationDto reservationDto = getReservationDto();

        // Mock del ModelMapper
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.any()))
                .thenReturn(new Reservation());

        // Simula la risposta del servizio
        Mockito.when(reservationService.save(Mockito.any(ReservationDto.class)))
                .thenReturn(reservationDto);

        // Usa l'ObjectMapper autowired
        String reservationJson = objectMapper.writeValueAsString(reservationDto);

        // Rimuovi l'operazione di salvataggio del DAO nel test
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/reservations/add/"+ loggedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endDate").value("2021-12-02T19:30:00")) // Use ISO 8601 format
                .andExpect(jsonPath("$.startDate").value("2021-12-01T17:00:00")); // Use ISO 8601 format

        // Verifica che il servizio sia stato chiamato con l'oggetto corretto
        Mockito.verify(reservationService).save(Mockito.any(ReservationDto.class));
    }

    private ReservationDto getReservationDto() {
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
        reservationDto.setParkingSpot(parkingSpotIdDto);
        reservationDto.setDriver(driver);
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setPrice(10.0);
        reservationDto.setLicensePlate(licensePlateIdDto);

        return reservationDto;
    }

    private ParkingSpotDto getParkingSpotDto() {
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

    private static ParkingSpaceDto getParkingSpaceDto() {
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


    @Test
    void testMappingReservationDtoToReservation() {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setPrice(50.0);
        UserIdDto userIdDto = new UserIdDto();
        userIdDto.setUserId(UUID.fromString("6cf3af64-b638-4c7a-bdd3-96cec1a849c5"));
        reservationDto.setDriver(userIdDto);

        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);

        //Assertions.assertNotNull(reservation);
        Assertions.assertEquals(reservationDto.getDriver().getUserId(), reservation.getDriver().getId());
    }


    /*
    @Test
    @WithMockCustomUser(name = "AdminUser", role = "ADMIN")
    void testSaveParkingSpace_AsAdmin() throws Exception {
        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();
        parkingSpaceDto.setName("Admin Parking");
        parkingSpaceDto.setAddress("456 Admin Street");
        parkingSpaceDto.setCity("Admin City");

        Mockito.when(parkingSpaceService.save(Mockito.any(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        String parkingSpaceJson = new ObjectMapper().writeValueAsString(parkingSpaceDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parkingSpaces/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin Parking"))
                .andExpect(jsonPath("$.address").value("456 Admin Street"));

        Mockito.verify(parkingSpaceService).save(Mockito.any(ParkingSpaceDto.class));
    }*/

}
