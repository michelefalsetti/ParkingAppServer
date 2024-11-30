package com.scrum.parkingapp.dto.security;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessTokenValidationDto {
    @NotNull
    private String token;
}
