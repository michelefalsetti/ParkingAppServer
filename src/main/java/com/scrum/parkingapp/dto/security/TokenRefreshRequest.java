package com.scrum.parkingapp.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRefreshRequest {

    @NotBlank(message = "Token is mandatory")
    @NotNull
    private String refreshToken;

}
