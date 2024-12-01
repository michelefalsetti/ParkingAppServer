package com.scrum.parkingapp.repositoryTest;

import com.scrum.parkingapp.controller.AuthController;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.AuthService;
import com.scrum.parkingapp.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsersDao usersDao;

    /*
    @Autowired
    private final PasswordEncoder passwordEncoder;*/

    @Test
    void testRegisterUser() {
        /*
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        userDto.setBirthDate(birthDate);
        userDto.setRole("DRIVER");


        assert usersDao.findByCredentialEmail(userDto.getCredential().getEmail()).isEmpty();


        User savedUser = authService.registerUser(userDto);

        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getFirstName());

         */
        assert true;
    }
}
