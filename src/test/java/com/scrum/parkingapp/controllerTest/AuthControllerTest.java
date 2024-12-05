package com.scrum.parkingapp.controllerTest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.config.security.SecurityConfig;
import com.scrum.parkingapp.config.security.filter.JwtAuthenticationFilter;
import com.scrum.parkingapp.controller.AuthController;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.AuthService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.CredentialDto;
import com.scrum.parkingapp.dto.SaveUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService; // Mock del servizio richiesto

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RevokedTokenService revokedTokenService;

    @MockBean
    private LoggedUserDetailsService loggedUserDetailsService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Dati di input
        SaveUserDto userDto = new SaveUserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setBirthDate(LocalDate.of(1990, 1, 1));
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setEmail("john@example.com");
        credentialDto.setPassword("SecurePass123!");
        userDto.setCredential(credentialDto);

        // Risposta mock del service
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setFirstName("John");
        userDetailsDto.setLastName("Doe");
        userDetailsDto.setEmail("john@example.com");
        userDetailsDto.setId(UUID.randomUUID());

        when(authService.registerUser(any(SaveUserDto.class)))
                .thenReturn(userDetailsDto);

        // Simula la richiesta HTTP
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        // Verifica che il service sia stato chiamato una volta
        verify(authService, times(1)).registerUser(any(SaveUserDto.class));
    }

    @Test
    void testLogin() throws Exception {
        // Credenziali di test
        String email = "test@example.com";
        String password = "Ciaobello!10";

        // DTO di input
        CredentialDto credentials = new CredentialDto();
        credentials.setEmail(email);
        credentials.setPassword(password);

        // Token simulati
        String accessToken = "mock-access-token";
        String refreshToken = "mock-refresh-token";

        // Simula il comportamento del servizio
        when(authService.loginUser(any(CredentialDto.class)))
                .thenReturn(Map.of("access_token", accessToken, "refresh_token", refreshToken));

        // Effettua la chiamata POST
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(credentials))) // Convertiamo il DTO in JSON
                .andExpect(status().isOk()) // Verifica che lo stato sia 200 OK
                .andExpect(jsonPath("$.access_token").value(accessToken)) // Verifica il token di accesso
                .andExpect(jsonPath("$.refresh_token").value(refreshToken)) // Verifica il token di refresh
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));

        // Verifica che il servizio sia stato chiamato
        verify(authService).loginUser(any(CredentialDto.class));
    }

    // Metodo per convertire un oggetto in JSON string
    private String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
