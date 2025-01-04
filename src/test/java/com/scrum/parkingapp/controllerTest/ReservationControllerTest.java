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
import com.scrum.parkingapp.utils.DatesGetter;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        DatesGetter datesGetter = new DatesGetter();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();


        ReservationDto reservationDto = datesGetter.getReservationDto(authentication);

        String strStartDate = "2025-12-01T17:00:00";
        String strEndDate =   "2025-12-02T19:30:00";

        // Mock del ModelMapper
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.any()))
                .thenReturn(new Reservation());

        // Simula la risposta del servizio
        Mockito.when(reservationService.save(Mockito.any(ReservationDto.class)))
                .thenReturn(reservationDto);

        // Usa l'ObjectMapper autowired
        String reservationJson = objectMapper.writeValueAsString(reservationDto);

        // Rimuovi l'operazione di salvataggio del DAO nel test

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/reservations/add/"+ loggedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endDate").value("2025-12-02T19:30:00"))
                .andExpect(jsonPath("$.startDate").value("2025-12-01T17:00:00"));


        // Verifica che il servizio sia stato chiamato con l'oggetto corretto
        Mockito.verify(reservationService).save(Mockito.any(ReservationDto.class));
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
