package com.scrum.parkingapp.serviceTest;


import com.scrum.parkingapp.data.dao.*;
import com.scrum.parkingapp.data.domain.SpotType;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.Reservation;
import com.scrum.parkingapp.data.service.implem.ParkingSpaceServiceImpl;
import com.scrum.parkingapp.data.service.implem.ParkingSpotServiceImpl;
import com.scrum.parkingapp.dto.ParkingSpotDto;
import com.scrum.parkingapp.utils.DatesGetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotServiceImplTest {

    @Mock
    private UsersDao usersDao;

    @Mock
    private ParkingSpaceDao parkingSpaceDao;

    @Mock
    private ParkingSpotDao parkingSpotDao;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ParkingSpotServiceImpl parkingSpotService;

    @InjectMocks
    private DatesGetter datesGetter; // Usa un'istanza di DatesGetter


    @Test
    void testSaveParkingSpot() {
        // Setup
        Long parkingSpaceId = 1L;
        ParkingSpotDto spotDto = new ParkingSpotDto();
        spotDto.setId(1L);
        spotDto.setParkingSpaceId(parkingSpaceId);
        spotDto.setBasePrice(10.0);
        spotDto.setNumber("A3");
        spotDto.setType(SpotType.CAR);

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(parkingSpaceId);
        parkingSpace.setParkingSpots(new ArrayList<>());

        ParkingSpot spot = new ParkingSpot();
        spot.setId(1L);
        spot.setParkingspaceId(parkingSpace);
        spot.setBasePrice(10.0);
        spot.setNumber("A3");
        spot.setType(SpotType.CAR);

        // Mock
        when(modelMapper.map(Mockito.any(ParkingSpotDto.class), Mockito.eq(ParkingSpot.class))).thenReturn(spot);
        //when(parkingSpaceDao.findById(parkingSpaceId)).thenReturn(Optional.of(parkingSpace));
        when(parkingSpotDao.save(Mockito.any(ParkingSpot.class))).thenReturn(spot);
        when(modelMapper.map(Mockito.any(ParkingSpot.class), Mockito.eq(ParkingSpotDto.class))).thenReturn(spotDto);

        // Execute
        ParkingSpotDto savedSpot = parkingSpotService.save(spotDto);

        // Verify
        verify(parkingSpotDao).save(Mockito.any(ParkingSpot.class));
        //verify(parkingSpaceDao).save(parkingSpace);

        assertNotNull(savedSpot);
        Assertions.assertEquals(1L, savedSpot.getId());
        assertEquals(parkingSpaceId, savedSpot.getParkingSpaceId());
        assertEquals("A3", savedSpot.getNumber());
        Assertions.assertEquals(10.0, savedSpot.getBasePrice());
    }

    @Test
    void testDeleteParkingSpot_Success() {
        Long spotId = 1L;
        ParkingSpot spot = new ParkingSpot();
        spot.setId(spotId);

        ParkingSpace space = new ParkingSpace();
        space.setId(1L);
        space.setParkingSpots(new ArrayList<>(Collections.singletonList(spot)));

        spot.setParkingspaceId(space);
        spot.setReservations(new ArrayList<>());

        when(parkingSpotDao.findById(spotId)).thenReturn(Optional.of(spot));
        when(parkingSpaceDao.findById(space.getId())).thenReturn(Optional.of(space));

        Boolean result = parkingSpotService.delete(spotId);

        assertTrue(result);
        verify(parkingSpotDao).delete(spot);
        verify(parkingSpotDao).flush();
        verify(parkingSpaceDao).save(space);
    }

    @Test
    void testDeleteParkingSpot_WithReservations() {
        Long spotId = 1L;
        ParkingSpot spot = new ParkingSpot();
        spot.setId(spotId);

        ParkingSpace space = new ParkingSpace();
        space.setId(1L);
        space.setParkingSpots(new ArrayList<>(Collections.singletonList(spot)));

        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        spot.setParkingspaceId(space);
        spot.setReservations(reservations);

        when(parkingSpotDao.findById(spotId)).thenReturn(Optional.of(spot));
        when(parkingSpaceDao.findById(space.getId())).thenReturn(Optional.of(space));

        Boolean result = parkingSpotService.delete(spotId);

        Assertions.assertTrue(result);
        verify(reservationDao).deleteAll(reservations);
        verify(reservationDao).flush();
        verify(parkingSpotDao).delete(spot);
        verify(parkingSpaceDao).save(space);
    }

    @Test
    void testDeleteParkingSpot_NotFound() {
        Long spotId = 1L;
        when(parkingSpotDao.findById(spotId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                parkingSpotService.delete(spotId)
        );
    }

    @Test
    void testDeleteParkingSpot_SpaceNotFound() {
        Long spotId = 1L;
        ParkingSpot spot = new ParkingSpot();
        spot.setId(spotId);

        ParkingSpace space = new ParkingSpace();
        space.setId(1L);
        spot.setParkingspaceId(space);

        when(parkingSpotDao.findById(spotId)).thenReturn(Optional.of(spot));
        when(parkingSpaceDao.findById(space.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                parkingSpotService.delete(spotId)
        );
    }
}
