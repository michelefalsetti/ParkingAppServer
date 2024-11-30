package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.dto.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordUserDto {

    @NotBlank(message = "Old password is required")
    @ValidPassword
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @ValidPassword
    private String newPassword;

}

