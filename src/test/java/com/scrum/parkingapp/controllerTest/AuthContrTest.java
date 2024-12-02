package com.scrum.parkingapp.controllerTest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrum.parkingapp.controller.AuthController;
import com.scrum.parkingapp.data.service.AuthService;
import com.scrum.parkingapp.dto.CredentialDto;
import com.scrum.parkingapp.dto.SaveUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import org.junit.Test;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ExtendWith(SpringExtension.class)
public class AuthContrTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testRegisterUser_Success() throws JsonProcessingException {
        SaveUserDto userDto = new SaveUserDto();
        String firstName = "John";
        String lastName = "Doe";
        String email = "samu@gm.com";
        String password = "Ciaobello!10";

        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setEmail(email);
        credentialDto.setPassword(password);
        userDto.setCredential(credentialDto);

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setFirstName(firstName);
        userDetailsDto.setLastName(lastName);
        userDetailsDto.setEmail(email);

        /*
        when(authService.registerUser(any(SaveUserDto.class)).thenReturn(userDetailsDto));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                .contentType(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
        */
    }


}
