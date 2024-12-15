package com.scrum.parkingapp.serviceTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.config.security.SecurityConfig;
import com.scrum.parkingapp.config.security.filter.JwtAuthenticationFilter;
import com.scrum.parkingapp.controller.AuthController;
import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.service.ParkingSpaceService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.data.service.implem.ParkingSpaceServiceImpl;
import com.scrum.parkingapp.dto.ParkingSpaceDto;
import com.scrum.parkingapp.utils.DatesGetter;
import com.scrum.parkingapp.utils.WithMockCustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class ParkingSpaceServiceImplTest {

    @Mock
    private UsersDao usersDao;

    @Mock
    private ParkingSpaceDao parkingSpaceDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ParkingSpaceServiceImpl parkingSpaceService;

    @InjectMocks
    private DatesGetter datesGetter; // Usa un'istanza di DatesGetter

    @Test
    @WithMockCustomUser(role = "OWNER")
    void testSaveParkingSpace_AsOwner() {
        // Dati di input per il test
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ParkingSpaceDto parkingSpaceDto = datesGetter.getParkingSpaceDto(null);

        // Simula il mapping tra DTO e entità
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setName("Test Parking");
        parkingSpace.setAddress("123 Test Street");
        parkingSpace.setCity("Test City");

        Mockito.when(modelMapper.map(Mockito.any(ParkingSpaceDto.class), Mockito.eq(ParkingSpace.class)))
                .thenReturn(parkingSpace);
        Mockito.when(parkingSpaceDao.save(Mockito.any(ParkingSpace.class)))
                .thenReturn(parkingSpace);
        Mockito.when(modelMapper.map(Mockito.any(ParkingSpace.class), Mockito.eq(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        // Esegui il metodo del servizio
        ParkingSpaceDto result = parkingSpaceService.save(parkingSpaceDto);

        // Verifica il risultato
        Mockito.verify(parkingSpaceDao).save(Mockito.any(ParkingSpace.class));
        Mockito.verify(modelMapper).map(Mockito.any(ParkingSpaceDto.class), Mockito.eq(ParkingSpace.class));
        Mockito.verify(modelMapper).map(Mockito.any(ParkingSpace.class), Mockito.eq(ParkingSpaceDto.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Parking", result.getName());
        Assertions.assertEquals("123 Test Street", result.getAddress());
    }

}


