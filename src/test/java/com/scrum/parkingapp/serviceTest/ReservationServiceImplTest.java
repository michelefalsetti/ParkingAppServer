package com.scrum.parkingapp.serviceTest;


import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.Reservation;
import com.scrum.parkingapp.data.entities.User;
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

import java.util.UUID;

@ExtendWith(MockitoExtension.class)

public class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private UsersDao userDao;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtService jwtService;

    @Test
    void testMappingReservationDtoToReservation() {
        // Dati di input
        DatesGetter datesGetter = new DatesGetter();
        ReservationDto reservationDto = datesGetter.getReservationDto(null);

        // Mapping con ModelMapper configurato
        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);

        // Asserzioni
        Assertions.assertNotNull(reservation);
        Assertions.assertNotNull(reservation.getUser());
        Assertions.assertEquals(reservationDto.getDriver().getId(), reservation.getUser().getId());
        Assertions.assertNotNull(reservation.getParkingSpot());
        //Assertions.assertEquals(reservationDto.getParkingSpotId(), reservation.getParkingSpot().getId());
        Assertions.assertNotNull(reservation.getLicensePlate());
        Assertions.assertEquals(reservationDto.getLicensePlateId(), reservation.getLicensePlate().getId());
        Assertions.assertEquals(reservationDto.getStartDate(), reservation.getStartDate());
        Assertions.assertEquals(reservationDto.getEndDate(), reservation.getEndDate());
    }


    @Test
    @WithMockCustomUser(role = "DRIVER")
    void shouldAddReservation() {
        // given
        DatesGetter datesGetter = new DatesGetter();
        ReservationDto reservationDto = datesGetter.getReservationDto(null);

        UUID driverId = UUID.randomUUID();
        String firstName = "Mario";
        String lastName = "Rossi";
        UserDto userDto = datesGetter.getUserDto(driverId, firstName, lastName,"DRIVER");

        User driver = modelMapper.map(userDto, User.class);
        LicensePlate licensePlate = modelMapper.map(reservationDto.getLicensePlateId(), LicensePlate.class);

        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.getId());
        reservation.setUser(driver);
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setPrice(reservationDto.getPrice());
        reservation.setLicensePlate(licensePlate);

        Mockito.when(modelMapper.map(Mockito.any(ReservationDto.class), Mockito.eq(Reservation.class)))
                .thenReturn(reservation);

        Mockito.when(reservationDao.save(Mockito.any(Reservation.class)))
                .thenReturn(reservation);
        Mockito.when(modelMapper.map(Mockito.any(Reservation.class), Mockito.eq(ReservationDto.class)))
                .thenReturn(reservationDto);

        ReservationDto result = reservationService.save(reservationDto);

        Mockito.verify(reservationDao).save(Mockito.any(Reservation.class));
        Mockito.verify(modelMapper).map(Mockito.any(ReservationDto.class), Mockito.eq(Reservation.class));
        Mockito.verify(modelMapper).map(Mockito.any(Reservation.class), Mockito.eq(ReservationDto.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(reservationDto.getId(), result.getId());
        Assertions.assertEquals(reservationDto.getStartDate(), result.getStartDate());
        Assertions.assertEquals(reservationDto.getEndDate(), result.getEndDate());
        Assertions.assertEquals(reservationDto.getPrice(), result.getPrice());
        //Assertions.assertEquals(reservationDto.getParkingSpotId(), result.getParkingSpotId());
        Assertions.assertEquals(reservationDto.getLicensePlateId(), result.getLicensePlateId());

        // when
        // then
    }

}
