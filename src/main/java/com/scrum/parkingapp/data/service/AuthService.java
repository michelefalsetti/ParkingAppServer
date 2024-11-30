package com.scrum.parkingapp.data.service;


import com.scrum.parkingapp.dto.CredentialDto;
import com.scrum.parkingapp.dto.SaveUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.security.AccessTokenValidationDto;

import java.util.Map;

public interface AuthService {
    UserDetailsDto registerUser(SaveUserDto userDto);

    UserDetailsDto registerOwner(SaveUserDto userDto);
    UserDetailsDto registerAdmin(SaveUserDto userDto);

    Map<String, String> loginUser(CredentialDto loginDto);


    Map<String, String> refreshToken(String authorizationHeader, String toString);

    AccessTokenValidationDto validateToken(AccessTokenValidationDto accessTokenValidationDto);
}
