package com.scrum.parkingapp.dto.security;

import lombok.Data;

@Data
public class RefreshTokenDto {
    private String token;

    public RefreshTokenDto() {}
    public RefreshTokenDto(String token){
        this.token = token;
    }

}
