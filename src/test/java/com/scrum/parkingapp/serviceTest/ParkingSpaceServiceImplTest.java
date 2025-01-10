package com.scrum.parkingapp.serviceTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.config.security.SecurityConfig;
import com.scrum.parkingapp.config.security.filter.JwtAuthenticationFilter;
import com.scrum.parkingapp.controller.AuthController;
import com.scrum.parkingapp.data.dao.AddressDao;
import com.scrum.parkingapp.data.dao.ParkingSpaceDao;
import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.*;
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

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ParkingSpaceServiceImplTest {

    @Mock
    private UsersDao usersDao;

    @Mock
    private ParkingSpaceDao parkingSpaceDao;

    @Mock
    private ParkingSpotDao parkingSpotDao;

    @Mock
    private AddressDao addressDao;


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

        Address address = new Address();

        address.setStreet("123 Test Street");
        address.setCity("Test City");
        address.setLatitude(39.123456);
        address.setLongitude(16.123456);

        addressDao.save(address);

        parkingSpace.setName("Test Parking");
        parkingSpace.setAddress(address);

        when(modelMapper.map(Mockito.any(ParkingSpaceDto.class), Mockito.eq(ParkingSpace.class)))
                .thenReturn(parkingSpace);
        when(parkingSpaceDao.save(Mockito.any(ParkingSpace.class)))
                .thenReturn(parkingSpace);
        when(modelMapper.map(Mockito.any(ParkingSpace.class), Mockito.eq(ParkingSpaceDto.class)))
                .thenReturn(parkingSpaceDto);

        // Esegui il metodo del servizio
        ParkingSpaceDto result = parkingSpaceService.save(parkingSpaceDto);

        // Verifica il risultato
        verify(parkingSpaceDao).save(Mockito.any(ParkingSpace.class));
        verify(modelMapper).map(Mockito.any(ParkingSpaceDto.class), Mockito.eq(ParkingSpace.class));
        verify(modelMapper).map(Mockito.any(ParkingSpace.class), Mockito.eq(ParkingSpaceDto.class));

        Assertions.assertNotNull(result);
        assertEquals("Test Parking", result.getName());
        assertEquals("123 Test Street", result.getAddress().getStreet());
        assertEquals("Test City", result.getAddress().getCity());
        assertEquals(39.123456, result.getAddress().getLatitude());
        assertEquals( 16.123456, result.getAddress().getLongitude());
        result.getParkingSpots().forEach(parkingSpotDto -> {
            assertNotNull(parkingSpotDto);
        });
    }

    @Test
    @WithMockCustomUser(role = "OWNER")
    void testDeleteParkingSpace_Success() {
        UUID userId = UUID.randomUUID();
        Long spaceId = 1L;

        User owner = new Owner();
        owner.setId(userId);

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(spaceId);
        parkingSpace.setUser(owner);

        List<ParkingSpot> spots = Arrays.asList(new ParkingSpot(), new ParkingSpot());
        parkingSpace.setParkingSpots(spots);

        when(parkingSpaceDao.findById(spaceId)).thenReturn(Optional.of(parkingSpace));

        boolean result = parkingSpaceService.delete(spaceId, userId);

        assertTrue(result);
        verify(parkingSpotDao).deleteAll(spots);
        verify(parkingSpotDao).flush();
        verify(parkingSpaceDao).delete(parkingSpace);
    }

    @Test
    @WithMockCustomUser(role = "OWNER")
    void testDeleteParkingSpace_NotFound() {
        Long spaceId = 1L;
        UUID userId = UUID.randomUUID();

        when(parkingSpaceDao.findById(spaceId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                parkingSpaceService.delete(spaceId, userId)
        );
    }

    @Test
    @WithMockCustomUser(role = "OWNER")
    void testDeleteParkingSpace_WrongOwner() {
        Long spaceId = 1L;
        UUID userId = UUID.randomUUID();
        UUID differentUserId = UUID.randomUUID();

        User owner = new Owner();
        owner.setId(differentUserId);

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(spaceId);
        parkingSpace.setUser(owner);

        when(parkingSpaceDao.findById(spaceId)).thenReturn(Optional.of(parkingSpace));

        assertThrows(IllegalArgumentException.class, () ->
                parkingSpaceService.delete(spaceId, userId)
        );
    }

    @Test
    @WithMockCustomUser(role = "OWNER")
    void testDeleteParkingSpace_NoSpots() {
        UUID userId = UUID.randomUUID();
        Long spaceId = 1L;

        User owner = new Owner();
        owner.setId(userId);

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(spaceId);
        parkingSpace.setUser(owner);
        parkingSpace.setParkingSpots(new ArrayList<>());

        when(parkingSpaceDao.findById(spaceId)).thenReturn(Optional.of(parkingSpace));

        boolean result = parkingSpaceService.delete(spaceId, userId);

        assertTrue(result);
        verify(parkingSpotDao).deleteAll(new ArrayList<>());
        verify(parkingSpotDao).flush();
        verify(parkingSpaceDao).delete(parkingSpace);
    }


    @Test
    void testFindAllByCityAndStartDateAndEndDate() {
        // Arrange
        String city = "City1";
        LocalDateTime startDate1 =  LocalDateTime.of(2025, 1, 30, 17, 0);
        LocalDateTime endDate1 = startDate1.plusHours(2);

        LocalDateTime startDate2 =  LocalDateTime.of(2025, 1, 30, 19, 0);
        LocalDateTime endDate2 = startDate2.plusHours(2);

        // Crea gli oggetti di test
        ParkingSpace space1 = createParkingSpace();
        ParkingSpace space2 = createParkingSpace();

        ParkingSpot spot1 = createParkingSpot(1L, space1);
        ParkingSpot spot2 = createParkingSpot(2L, space2);

        Reservation reservation1 = createReservation(1L, spot1, startDate1, endDate1);
        Reservation reservation2 = createReservation(2L, spot2, startDate2, endDate2);

        spot1.setReservations(Collections.singletonList(reservation1));
        spot2.setReservations(Collections.singletonList(reservation2));

        // Prepara i risultati attesi dal DAO
        List<Object[]> mockResults1 =  new ArrayList<>();
        List<Object[]> mockResults2 =  new ArrayList<>();

        mockResults1.add(new Object[]{space1, spot1});
        mockResults2.add(new Object[]{space2, spot2});

        // Configura il mock del DAO
        when(parkingSpaceDao.findParkingSpacesAndAvailableSpots(city, startDate2, endDate2))
                .thenReturn(mockResults1);

        when(parkingSpaceDao.findParkingSpacesAndAvailableSpots(city, startDate1, endDate1))
                .thenReturn(mockResults2);

        // Configura il ModelMapper per la conversione in DTO
        ParkingSpaceDto expectedDto = new ParkingSpaceDto();
        when(modelMapper.map(any(ParkingSpace.class), eq(ParkingSpaceDto.class)))
                .thenReturn(expectedDto);

        // Act
        List<ParkingSpaceDto> result1 = parkingSpaceService.findAllByCityAndStartDateAndEndDate(
                city, startDate1, endDate1);

        List<ParkingSpaceDto> result2 = parkingSpaceService.findAllByCityAndStartDateAndEndDate(
                city, startDate2, endDate2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);

        assertEquals(1, result1.size()); // Dovrebbe esserci un solo ParkingSpace
        assertEquals(1, result2.size()); // Dovrebbe esserci un solo ParkingSpace

        verify(parkingSpaceDao).findParkingSpacesAndAvailableSpots(city, startDate1, endDate1);
        verify(parkingSpaceDao).findParkingSpacesAndAvailableSpots(city, startDate2, endDate2);

        verify(modelMapper, times(2)).map(any(ParkingSpot.class), eq(ParkingSpotDto.class));
    }

    @Test
    void testFindAllByCityAndStartDateAndEndDate_EmptyResult() {
        // Arrange

        String city = "City1";
        LocalDateTime startDate =  LocalDateTime.of(2025, 1, 30, 17, 0);
        LocalDateTime endDate = startDate.plusHours(2);

        // Configura il mock del DAO
        when(parkingSpaceDao.findParkingSpacesAndAvailableSpots(city, startDate, endDate))
                .thenReturn(Collections.emptyList());

        List<ParkingSpaceDto> result = parkingSpaceService.findAllByCityAndStartDateAndEndDate(
                city, startDate, endDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(parkingSpaceDao).findParkingSpacesAndAvailableSpots(city, startDate, endDate);

    }

    private ParkingSpace createParkingSpace() {
        ParkingSpace space = new ParkingSpace();
        //space.setId(1L);
        Address address = new Address();
        address.setCity("City1");
        space.setAddress(address);
        return space;
    }

    private ParkingSpot createParkingSpot(Long id, ParkingSpace space) {
        ParkingSpot spot = new ParkingSpot();
        //spot.setId(id);
        spot.setParkingspaceId(space);
        return spot;
    }

    private Reservation createReservation(Long id, ParkingSpot spot, LocalDateTime startDate, LocalDateTime endDate) {
        Reservation reservation = new Reservation();
        //reservation.setId(id);
        reservation.setPrice(10.0);
        reservation.setLicensePlate("AB123CD");
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setParkingSpot(spot);
        return reservation;
    }
}


