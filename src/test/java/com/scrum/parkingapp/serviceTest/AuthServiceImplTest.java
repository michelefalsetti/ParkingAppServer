package com.scrum.parkingapp.serviceTest;

import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.Credential;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.implem.AuthServiceImpl;
import com.scrum.parkingapp.dto.CredentialDto;
import com.scrum.parkingapp.dto.SaveUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import com.scrum.parkingapp.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsersDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Input
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setBirthDate(LocalDate.of(1990, 1, 1));
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setEmail("john@example.com");
        credentialDto.setPassword("SecurePass123!");
        userDto.setCredential(credentialDto);

        // Mock password hashing
        String hashedPassword = "hashed_password";
        when(passwordEncoder.encode("SecurePass123!")).thenReturn(hashedPassword);

        // Mock user creation
        User user = new User();
        user.setCredential(new Credential());
        user.getCredential().setPassword(hashedPassword);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);

        // Mock salvataggio utente
        when(userDao.save(user)).thenReturn(user);

        // Mock risposta finale
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setFirstName("John");
        userDetailsDto.setLastName("Doe");
        userDetailsDto.setEmail("john@example.com");
        when(modelMapper.map(user, UserDetailsDto.class)).thenReturn(userDetailsDto);

        // Chiama il metodo
        UserDetailsDto result = authService.registerUser(userDto);

        // Verifica il risultato
        Assertions.assertNotNull(result);
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Doe", result.getLastName());
        Assertions.assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Input
        SaveUserDto userDto = new SaveUserDto();
        userDto.setCredential(new CredentialDto());
        userDto.getCredential().setEmail("existing@example.com");

        // Mock che simula un utente già esistente
        when(userDao.findByCredentialEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        // Verifica che venga lanciata un'eccezione
        assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(userDto));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        // Input
        String email = "test@example.com";
        String password = "Ciaobello!10";

        // Dati simulati
        CredentialDto credentials = new CredentialDto();
        credentials.setEmail(email);
        credentials.setPassword(password);

        User user = new User();
        Credential credential = new Credential();
        credential.setEmail(email);
        credential.setPassword(password);
        user.setCredential(credential);
        user.setId(UUID.randomUUID());

        LoggedUserDetails userDetails = new LoggedUserDetails(user);
        String accessToken = "mock-access-token";
        String refreshToken = "mock-refresh-token";

        // Mock delle dipendenze

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(userDetails)).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(userDetails, 96)).thenReturn(refreshToken);

        RefreshTokenDto savedRefreshToken = new RefreshTokenDto(refreshToken);  // Simuliamo che venga salvato il refresh token
        when(refreshTokenService.save(any(RefreshTokenDto.class), eq(userDetails))).thenReturn(savedRefreshToken);



        // Esegui il metodo
        Map<String, String> tokens = authService.loginUser(credentials);

        // Verifica il risultato
        Assertions.assertNotNull(tokens);
        Assertions.assertEquals(accessToken, tokens.get("access_token"));
        Assertions.assertEquals(refreshToken, tokens.get("refresh_token"));

        // Verifica che le dipendenze siano state chiamate
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
        verify(jwtService).generateRefreshToken(userDetails, 96);
        verify(refreshTokenService).save(any(RefreshTokenDto.class), eq(userDetails));
    }
}
