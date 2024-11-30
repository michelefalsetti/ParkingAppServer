package com.scrum.parkingapp.data.service;

import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshTokenDto save(RefreshTokenDto refreshTokenDto, UserDetails userDetails);

    RefreshTokenDto getTokenByUserId(UUID userId);

    void revokeRefreshTokenByUserId(UUID userId);

    RefreshTokenDto findByToken(UUID userId, String refreshToken);

    void revokeRefreshTokenByToken(UUID userId, String token);

    List<RefreshTokenDto> getAll();
}
