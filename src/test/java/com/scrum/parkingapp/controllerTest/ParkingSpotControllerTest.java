package com.scrum.parkingapp.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.config.ModelMapperConfig;
import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.config.security.SecurityConfig;
import com.scrum.parkingapp.controller.ParkingSpaceController;
import com.scrum.parkingapp.controller.ParkingSpotController;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.data.service.ParkingSpotService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.dto.ParkingSpaceIdDto;
import com.scrum.parkingapp.dto.ParkingSpotDto;
import com.scrum.parkingapp.dto.UserIdDto;
import com.scrum.parkingapp.utils.WithMockCustomUser;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ParkingSpotController.class) // Cambia qui
@AutoConfigureMockMvc
@Import({SecurityConfig.class, ModelMapperConfig.class})
@ActiveProfiles("test")
public class ParkingSpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingSpotService parkingSpotService; // Cambia in ParkingSpotService

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
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "OWNER")
    void testAddSpot_Owner() throws Exception {
        ParkingSpotDto parkingSpotDto = getParkingSpotDto();

        // Mock del ModelMapper
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.any()))
                .thenReturn(new ParkingSpace());

        // Simula la risposta del servizio
        Mockito.when(parkingSpotService.save(Mockito.any(ParkingSpotDto.class)))
                .thenReturn(parkingSpotDto);

        // Usa l'ObjectMapper autowired
        String parkingSpaceJson = objectMapper.writeValueAsString(parkingSpotDto);

        // Rimuovi l'operazione di salvataggio del DAO nel test
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parkingSpots/add/"+ loggedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("TEST-1"));
                //.andExpect(jsonPath("$.address").value("123 Test Street"));

        // Verifica che il servizio sia stato chiamato con l'oggetto corretto
        Mockito.verify(parkingSpotService).save(Mockito.any(ParkingSpotDto.class));
    }

    private ParkingSpotDto getParkingSpotDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoggedUserDetails loggedUser = (LoggedUserDetails) authentication.getPrincipal();

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
