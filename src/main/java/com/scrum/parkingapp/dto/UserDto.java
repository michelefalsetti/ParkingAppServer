package com.scrum.parkingapp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;


    private String role;

}
