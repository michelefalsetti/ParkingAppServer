package com.scrum.parkingapp.ServiceTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.utils.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ParkingSpaceServiceImplTest {

    @Mock
    private UsersDao usersDao;

    @Mock
    private ParkingSpaceDao parkingSpaceDao;

    @Mock
    private ModelMapper modelMapper;


    /*
    @Test
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "OWNER")
    void testSaveParkingSpace_AsOwner() throws Exception {
        // Dati di input per il test
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setName("Test Parking");
        parkingSpaceDto.setAddress("123 Test Street");
        parkingSpaceDto.setCity("Test City");

        // Simula la risposta del servizio
        Mockito.when(parkingSpaceService.save(Mockito.any(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        // Converte l'oggetto in JSON
        String parkingSpaceJson = new ObjectMapper().writeValueAsString(parkingSpaceDto);

        // Simula la chiamata POST al controller
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpaces/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpaceJson))
                .andExpect(status().isOk()) // Verifica che lo stato sia 200 OK
                .andExpect(jsonPath("$.name").value("Test Parking")) // Verifica il nome del parcheggio
                .andExpect(jsonPath("$.address").value("123 Test Street"));

        // Verifica che il servizio sia stato chiamato con l'oggetto corretto
        Mockito.verify(parkingSpaceService).save(Mockito.any(ParkingSpaceDto.class));
    }

    @Test
    @WithMockCustomUser(name = "AdminUser", role = "ADMIN")
    void testSaveParkingSpace_AsAdmin() throws Exception {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setName("Admin Parking");
        parkingSpaceDto.setAddress("456 Admin Street");
        parkingSpaceDto.setCity("Admin City");

        Mockito.when(parkingSpaceService.save(Mockito.any(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        String parkingSpaceJson = new ObjectMapper().writeValueAsString(parkingSpaceDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpaces/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin Parking"))
                .andExpect(jsonPath("$.address").value("456 Admin Street"));

        Mockito.verify(parkingSpaceService).save(Mockito.any(ParkingSpaceDto.class));
    }*/
}
