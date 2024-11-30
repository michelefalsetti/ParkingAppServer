package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.dto.validation.ValidBirthDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SaveUserDto {

    @NotBlank(message = "First Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name must contain only letters and spaces")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must contain only letters and spaces")
    private String lastName;

    @NotNull(message = "Birth Date is required")
    @ValidBirthDate(message = "Birth Date must be between 1910 and today minus 13 years")
    private LocalDate birthDate;

    @Valid
    @NotNull(message = "Credentials are required")
    private CredentialDto credential;


}
