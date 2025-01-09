package com.scrum.parkingapp.controllerTest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.config.ModelMapperConfig;
import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.config.security.SecurityConfig;
import com.scrum.parkingapp.controller.AuthController;
import com.scrum.parkingapp.controller.ParkingSpaceController;
import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.AuthService;
import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.*;
import com.scrum.parkingapp.utils.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.jsonwebtoken.impl.lang.Services.get;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ParkingSpaceController.class) // Cambia qui
@AutoConfigureMockMvc
@Import({SecurityConfig.class, ModelMapperConfig.class})
@ActiveProfiles("test")
public class ParkingSpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingSpaceService parkingSpaceService;

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

    private ParkingSpaceDto getParkingSpaceDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setLatitude(39.123456);
        addressDto.setLongitude(16.123456);
        addressDto.setStreet("Test Street");

        parkingSpaceDto.setId(1L);
        parkingSpaceDto.setName("Test Parking");
        parkingSpaceDto.setAddress(addressDto);

        UserIdDto owner = new UserIdDto();
        owner.setUserId(loggedUser.getId());
        parkingSpaceDto.setUserId(owner);

        // Aggiungiamo altri campi che potrebbero essere necessari

        return parkingSpaceDto;
    }

    // Aggiungiamo un metodo di utilità per stampare il contenuto del DTO come JSON
    private void printJson(ParkingSpaceDto dto) {
        try {
            System.out.println("DTO as JSON: " + objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "owner.rossi@example.com", role = "OWNER")
    void testAddParkingSpace_AsOwner() throws Exception {
        // Get the authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();


        // Assicuriamoci che il servizio mockato ritorni un valore valido
        when(parkingSpaceService.save(any(ParkingSpaceDto.class))).thenReturn(parkingSpaceDto);

        // Nota il path corretto "/api/v1/parkingSpaces/add/"
        MvcResult rightResult = mockMvc.perform(post("/api/v1/parkingSpaces/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(parkingSpaceDto)))
                .andDo(print()) // Aggiungiamo print() per vedere l'output dettagliato
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Parking"))
                .andExpect(jsonPath("$.address.city").value("Test City"))
                .andExpect(jsonPath("$.address.street").value("Test Street"))
                .andExpect(jsonPath("$.address.latitude").value(39.123456))
                .andExpect(jsonPath("$.address.longitude").value(16.123456))

                .andReturn();

        // Stampiamo eventuali eccezioni
        if (rightResult.getResolvedException() != null) {
            System.out.println("Exception: " + rightResult.getResolvedException().getMessage());
        }

        verify(parkingSpaceService, times(1)).save(any(ParkingSpaceDto.class));
    }

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "owner.rossi@example.com", role = "OWNER")
    void testAddParkingSpace_DifferentUserId() throws Exception {
        // Get the authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();


        ParkingSpaceDto unAuthorizedParkingSpaceDto = getParkingSpaceDto();

        unAuthorizedParkingSpaceDto.getUserId().setUserId(UUID.randomUUID());


        // Nota il path corretto "/api/v1/parkingSpaces/add/"
        MvcResult result = mockMvc.perform(post("/api/v1/parkingSpaces/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(unAuthorizedParkingSpaceDto)))
                .andDo(print()) // Aggiungiamo print() per vedere l'output dettagliato
                .andExpect(status().is(500))

                .andReturn();

        // Stampiamo eventuali eccezioni
        if (result.getResolvedException() != null) {
            System.out.println("Exception: " + result.getResolvedException().getMessage());
        }

        verify(parkingSpaceService, times(0)).save(any(ParkingSpaceDto.class));
    }

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "driver.rossi@example.com", role = "DRIVER")
    void testAddParkingSpace_AsDriver() throws Exception {
        // Get the authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();

        // Assicuriamoci che il servizio mockato ritorni un valore valido
        when(parkingSpaceService.save(any(ParkingSpaceDto.class))).thenReturn(parkingSpaceDto);

        // Nota il path corretto "/api/v1/parkingSpaces/add/"
        MvcResult rightResult = mockMvc.perform(post("/api/v1/parkingSpaces/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(parkingSpaceDto)))
                .andDo(print()) // Aggiungiamo print() per vedere l'output dettagliato
                .andExpect(status().is(500))
                .andReturn();

        // Stampiamo eventuali eccezioni
        if (rightResult.getResolvedException() != null) {
            System.out.println("Exception: " + rightResult.getResolvedException().getMessage());
        }

        verify(parkingSpaceService, times(0)).save(any(ParkingSpaceDto.class));
    }



    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "DRIVER")
    void testGetBySearch() throws Exception {
        // Arrange
        String city = "Test City";
        String startDate = "2025-01-29T10:00:00";
        String endDate = "2025-01-30T10:00:00";

        // Crea una lista di ParkingSpaceDto di esempio
        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();
        //ParkingSpotDto parkingSpotDto = new ParkingSpotDto();

        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(1L);
        parkingSpotDto.setBasePrice(10.0);
        parkingSpotDto.setNumber("A1");
        parkingSpotDto.setParkingSpaceId(1L);

        List<ParkingSpotDto> parkingSpots = new ArrayList<>();
        parkingSpots.add(parkingSpotDto);

        parkingSpaceDto.setParkingSpots(parkingSpots);

        List<ParkingSpaceDto> mockDtos =  List.of(
                parkingSpaceDto
        );

        // Configura il mock del service
        when(parkingSpaceService.findAllByCityAndStartDateAndEndDate(
                eq(city),
                eq(LocalDateTime.parse(startDate)),
                eq(LocalDateTime.parse(endDate))
        )).thenReturn(mockDtos);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parkingSpaces/getBySearch/" +
                                "{city}/{startDate}/{endDate}", city, startDate, endDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Parking") )
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].address.city").value(city))
                .andExpect(jsonPath("$[0].parkingSpots", hasSize(1))
                )

                //.andExpect(jsonPath("$[0].", hasSize(1))

                //.andExpect(jsonPath("$[1].id").value(2))
                //.andExpect(jsonPath("$[1].address.city").value(city))
                .andDo(print()); // Utile per il debug
    }

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "DRIVER")
    void testGetBySearch_NoContent() throws Exception {
        // Arrange
        String city = "Test City";
        String inputStartDate = "2025-01-29T10:00:00";
        String inputEndDate = "2025-01-29T12:00:00";

        // Configura il mock del service per restituire una lista vuota
        // poiché non ci dovrebbero essere spot disponibili in questo periodo
        when(parkingSpaceService.findAllByCityAndStartDateAndEndDate(
                eq(city),
                eq(LocalDateTime.parse(inputStartDate)),
                eq(LocalDateTime.parse(inputEndDate))
        )).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parkingSpaces/getBySearch/" +
                                "{city}/{startDate}/{endDate}", city, inputStartDate, inputEndDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "driver.rossi@example.com", role = "OWNER")
    void testRemoveParkingSpace_AsOwner() throws Exception {
        // Get the authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();

        // Assicuriamoci che il servizio mockato ritorni un valore valido
        when(parkingSpaceService.delete(Mockito.anyLong(), Mockito.any(UUID.class))).thenReturn(true);

        // Nota il path corretto "/api/v1/parkingSpaces/add/"
        MvcResult rightResult = mockMvc.perform(delete("/api/v1/parkingSpaces/delete/" + parkingSpaceDto.getId()
                + "/" + loggedUser.getId()))
                .andDo(print()) // Aggiungiamo print() per vedere l'output dettagliato
                .andExpect(status().is(200))
                .andReturn();

        // Stampiamo eventuali eccezioni
        if (rightResult.getResolvedException() != null) {
            System.out.println("Exception: " + rightResult.getResolvedException().getMessage());
        }

        verify(parkingSpaceService, times(1)).delete(Mockito.anyLong(), Mockito.any(UUID.class));
    }

    /*

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "DRIVER")
    void testGetBySearch_InvalidDateFormat() throws Exception {
        // Arrange
        String city = "Milano";
        String startDate = "invalid-date";
        String endDate = "2025-01-04T10:00:00";

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parkingSpaces/getBySearch/" +
                                "{city}/{startDate}/{endDate}", city, startDate, endDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }*/


    /*
    @Test
    @WithMockCustomUser(name = "AdminUser", role = "ADMIN")
    void testAddParkingSpace_AsAdmin() throws Exception {
        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();
        parkingSpaceDto.setName("Admin Parking");

        AddressDto  addressDto = new AddressDto();
        addressDto.setCity("Test City");
        addressDto.setLatitude(39.123456);
        addressDto.setLongitude(16.123456);
        addressDto.setStreet("Test Street");

        parkingSpaceDto.setAddress(addressDto);


        when(parkingSpaceService.save(any(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        String parkingSpaceJson = new ObjectMapper().writeValueAsString(parkingSpaceDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parkingSpaces/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin Parking"))
                .andExpect(jsonPath("$.address.street").value("Test Street"))
                .andExpect(jsonPath("$.address.city").value("Test City"))
                .andExpect(jsonPath("$.address.latitude").value(39.123456))
                .andExpect(jsonPath("$.address.longitude").value(16.123456));

        Mockito.verify(parkingSpaceService).save(any(ParkingSpaceDto.class));
    }*/

}
