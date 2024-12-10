package com.scrum.parkingapp.controllerTest;


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
import com.scrum.parkingapp.dto.ParkingSpaceDto;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "OWNER")
    void testAddParkingSpace_AsOwner() throws Exception {
        ParkingSpaceDto parkingSpaceDto = getParkingSpaceDto();

        // Mock del ModelMapper
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.any()))
                .thenReturn(new ParkingSpace());

        // Simula la risposta del servizio
        Mockito.when(parkingSpaceService.save(Mockito.any(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        // Usa l'ObjectMapper autowired
        String parkingSpaceJson = objectMapper.writeValueAsString(parkingSpaceDto);

        // Rimuovi l'operazione di salvataggio del DAO nel test
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parkingSpaces/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Parking"))
                .andExpect(jsonPath("$.address").value("123 Test Street"));

        // Verifica che il servizio sia stato chiamato con l'oggetto corretto
        Mockito.verify(parkingSpaceService).save(Mockito.any(ParkingSpaceDto.class));
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
    }

}
