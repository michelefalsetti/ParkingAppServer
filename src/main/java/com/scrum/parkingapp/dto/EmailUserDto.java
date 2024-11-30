package com.scrum.parkingapp.dto;

import com.scrum.parkingapp.dto.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailUserDto {

    @NotBlank(message = "Email is required")
    @ValidEmail(message = "Email should be valid")
    private String newEmail;

}
