package com.scrum.parkingapp.data.service;

public interface RevokedTokenService {
     void revokeToken(String token);
     boolean isTokenRevoked(String token);
}
