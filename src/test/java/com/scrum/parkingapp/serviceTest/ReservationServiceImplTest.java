package com.scrum.parkingapp.serviceTest;


import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.data.dao.AddressDao;
import com.scrum.parkingapp.data.dao.ParkingSpotDao;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.*;
import com.scrum.parkingapp.data.service.ReservationService;
import com.scrum.parkingapp.data.service.implem.ReservationServiceImpl;
import com.scrum.parkingapp.dto.ReservationDto;
import com.scrum.parkingapp.dto.UserDto;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;
    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ParkingSpotDao parkingSpotDao;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private DatesGetter datesGetter; // Usa un'istanza di DatesGetter

    @Mock
    private AddressDao addressDao;

    @Mock
    private UsersDao usersDao;




    @Test
    void testSaveReservationSuccessfully() {
        // Creare ID e oggetti di test
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReservationDto inputDto = datesGetter.getReservationDto(null);

        Reservation reservation = new Reservation();

        reservation.setStartDate(inputDto.getStartDate());
        reservation.setEndDate(inputDto.getEndDate());
        reservation.setLicencePlate(inputDto.getLicensePlate());
        reservation.setPrice(inputDto.getPrice());

        User user = new User();
        user.setId(inputDto.getUser().getId());
        reservation.setUser(user);

        ParkingSpot parkingSpot = datesGetter.getParkingSpot(authentication, 1L);

        parkingSpotDao.save(parkingSpot);

        reservation.setParkingSpot(parkingSpot);

        // Configurare i mock

        when(usersDao.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(user));
        when(parkingSpotDao.findById(Mockito.any())).thenReturn(Optional.of(parkingSpot));

        when(reservationDao.save(Mockito.any(Reservation.class))).thenReturn(reservation);
        when(usersDao.save(Mockito.any(User.class))).thenReturn(reservation.getUser());
        when(parkingSpotDao.save(Mockito.any(ParkingSpot.class))).thenReturn(parkingSpot);

        when(modelMapper.map(Mockito.any(Reservation.class), eq(ReservationDto.class))).thenReturn(inputDto);

        // Eseguire il test
        ReservationDto result = reservationService.save(inputDto);

        // Verifiche
        verify(usersDao).findById(Mockito.any(UUID.class));
        verify(parkingSpotDao).findById(Mockito.any());
        verify(reservationDao).save(Mockito.any(Reservation.class));
        verify(usersDao).save(Mockito.any(User.class));
        verify(parkingSpotDao, times(2)).save(Mockito.any(ParkingSpot.class));
        verify(modelMapper).map(Mockito.any(Reservation.class), eq(ReservationDto.class));

        assertEquals(inputDto.getPrice(), result.getPrice());
        assertEquals(inputDto.getLicensePlate(), result.getLicensePlate());
        assertEquals(inputDto.getStartDate(), result.getStartDate());
        assertEquals(inputDto.getEndDate(), result.getEndDate());
        assertEquals(inputDto.getUser().getId(), result.getUser().getId());
        assertEquals(inputDto.getParkingSpotId(), result.getParkingSpotId());

    }


    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        ReservationDto inputDto = createReservationDto();
        when(usersDao.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.save(inputDto), // Lambda per differire l'esecuzione
                "Expected IllegalArgumentException when user ID is invalid" // Messaggio descrittivo
        );

        // Verifica il messaggio dell'eccezione
        assertEquals("Invalid user ID", exception.getMessage());

        // Verifica che il metodo corretto sia stato chiamato
        verify(usersDao).findById(Mockito.any(UUID.class));
        verify(parkingSpotDao, never()).findById( Mockito.any(Long.class));  // Non deve essere chiamato
        verify(reservationDao, never()).save(Mockito.any(Reservation.class));  // Non deve essere chiamato
    }

    @Test
    void shouldThrowExceptionWhenParkingSpotNotFound() {
        // Arrange
        ReservationDto inputDto = createReservationDto();
        User user = createUser();

        when(usersDao.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(user));
        when(parkingSpotDao.findById(Mockito.any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        reservationService.save(inputDto),
                "Invalid parking spot ID"
        );

        verify(usersDao).findById(Mockito.any(UUID.class));

        verify(parkingSpotDao).findById(Mockito.any());
        verify(reservationDao, never()).save(Mockito.any(Reservation.class));
    }

    /*
    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {
        // Arrange
        ReservationDto inputDto = createInvalidDateReservationDto();
        User user = createUser();
        ParkingSpot parkingSpot = createParkingSpot();

        when(usersDao.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(parkingSpotDao.findById(any())).thenReturn(Optional.of(parkingSpot));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        reservationService.save(inputDto),
                "Start date must be before end date"
        );
    }*/

    // Helper methods
    private ReservationDto createReservationDto() {
        ReservationDto dto = new ReservationDto();
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID()); // Imposta un ID valido
        dto.setUser(userDto);
        dto.setParkingSpotId(1L);
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusHours(2));
        dto.setLicensePlate("ABC123");
        return dto;
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Credential credential = new Credential();
        credential.setEmail("test@example.com");
        credential.setPassword("testUser");
        user.setCredential(credential);
        // Imposta altri campi necessari
        return user;
    }

    private ParkingSpot createParkingSpot() {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(1L);
        // Imposta altri campi necessari
        return spot;
    }

    private Reservation createReservation(User user, ParkingSpot parkingSpot) {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setParkingSpot(parkingSpot);
        reservation.setStartDate(LocalDateTime.now());
        reservation.setEndDate(LocalDateTime.now().plusHours(2));
        reservation.setLicencePlate("ABC123");
        return reservation;
    }

    private ReservationDto createExpectedReservationDto() {
        ReservationDto dto = new ReservationDto();
        dto.setId(1L);
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        dto.setUser(userDto);
        dto.setParkingSpotId(1L);
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusHours(2));
        dto.setLicensePlate("ABC123");
        return dto;
    }
}
