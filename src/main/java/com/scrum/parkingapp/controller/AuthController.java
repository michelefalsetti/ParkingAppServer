package com.scrum.parkingapp.controller;


import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import com.scrum.parkingapp.data.service.AuthService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.CredentialDto;
import com.scrum.parkingapp.dto.SaveUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.security.AccessTokenValidationDto;
import com.scrum.parkingapp.dto.security.AuthenticationResponse;
import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import com.scrum.parkingapp.dto.security.TokenRefreshRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    private final RevokedTokenService revokedTokenService;
    private final LoggedUserDetailsService loggedUserDetailsService;


    @PostMapping(consumes = "application/json", path = "/register")
    public ResponseEntity<UserDetailsDto> registerUser(@Valid @RequestBody SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }

    @PostMapping(consumes = "application/json", path = "/registerOwner")
    public ResponseEntity<UserDetailsDto> registerOwner(@Valid @RequestBody SaveUserDto userDto) {
        return ResponseEntity.ok(authService.registerOwner(userDto));
    }

    @PostMapping(consumes = "application/json", path = "/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody CredentialDto credentials) {
        log.info("Received request for auth/login");
        return ResponseEntity.ok(authService.loginUser(credentials));
    }

    /*@GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                Map<String, String> tokenMap = authService.refreshToken(authorizationHeader, request.getRequestURL().toString());
                response.addHeader("access_token", tokenMap.get("access_token"));
                response.addHeader("refresh_token", tokenMap.get("refresh_token"));
            }
            catch (Exception e) {
                //log.error(String.format("Error refresh token: %s", authorizationHeader), e);
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("errorMessage", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }*/
    @PostMapping("/{userId}/refreshToken")
    public ResponseEntity<?> refreshToken(@PathVariable("userId") UUID userId, @Valid @RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        RefreshTokenDto storedToken = refreshTokenService.findByToken(userId, refreshToken);

        if (storedToken == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }

        revokedTokenService.isTokenRevoked(storedToken.toString());

        if (jwtService.isTokenExpired(refreshToken)) {
            refreshTokenService.revokeRefreshTokenByToken(userId, refreshToken);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token is expired");
        }

        String username = jwtService.extractUsername(refreshToken);
        final UserDetails userDetails = loggedUserDetailsService.loadUserByUsername(username);

        final String newAccessToken = jwtService.generateToken(userDetails);
        final String newRefreshToken = jwtService.generateRefreshToken(userDetails, 96);

        refreshTokenService.revokeRefreshTokenByToken(userId, refreshToken);
        refreshTokenService.save(new RefreshTokenDto(newRefreshToken), userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, newRefreshToken));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<AccessTokenValidationDto> validateToken(@RequestBody AccessTokenValidationDto request) {
        return ResponseEntity.ok(authService.validateToken(request));
    }

}
