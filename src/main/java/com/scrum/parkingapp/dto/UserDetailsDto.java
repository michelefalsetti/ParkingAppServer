package com.scrum.parkingapp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDetailsDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

}
