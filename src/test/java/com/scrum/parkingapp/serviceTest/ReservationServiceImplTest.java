package com.scrum.parkingapp.ServiceTest;


import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.data.dao.ReservationDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.service.implem.ReservationServiceImpl;
import com.scrum.parkingapp.dto.LicensePlateIdDto;
import com.scrum.parkingapp.dto.ReservationDto;
import com.scrum.parkingapp.dto.UserDto;
import com.scrum.parkingapp.dto.UserIdDto;
import com.scrum.parkingapp.utils.DatesGetter;
import com.scrum.parkingapp.utils.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
    @WithMockCustomUser(name = "Mario", lastname = "Rossi", email = "mario.rossi@example.com", role = "DRIVER")
    void shouldAddReservation() {
        // given
        ReservationDto reservationDto = DatesGetter.getReservationDto();



        // when
        // then
    }

}
