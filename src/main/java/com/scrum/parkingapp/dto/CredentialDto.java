package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.dto.validation.ValidEmail;
import com.scrum.parkingapp.dto.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CredentialDto {

    @NotBlank(message = "Email is required")
    @ValidEmail(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

}
