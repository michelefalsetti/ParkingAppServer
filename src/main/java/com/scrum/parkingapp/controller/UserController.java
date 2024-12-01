package com.scrum.parkingapp.controller;


import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.data.service.UserService;
import com.scrum.parkingapp.dto.EmailUserDto;
import com.scrum.parkingapp.dto.PasswordUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RevokedTokenService revokedTokenService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable UUID id) {
        UserDetailsDto user = userService.getUserDetailsById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsDto>> allUsers() {
        List<UserDetailsDto> users = userService.getAllDto();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping(value = "{userId}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updatePassword(@PathVariable UUID userId,@Valid @RequestBody PasswordUserDto password){

        UserDto updatedUser= userService.updatePassword(userId, password);
        String toRevoke = refreshTokenService.getTokenByUserId(userId).getToken();
        revokedTokenService.revokeToken(toRevoke);
        refreshTokenService.revokeRefreshTokenByUserId(userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }


    @PutMapping(value = "{userId}/change-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<UserDto> updateEmail(@PathVariable UUID userId, @Valid @RequestBody EmailUserDto userDto){
        log.info("Received request for user/change-email");

        UserDto updatedUser= userService.updateEmail(userId, userDto);
        String toRevoke = refreshTokenService.getTokenByUserId(userId).getToken();
        revokedTokenService.revokeToken(toRevoke);
        refreshTokenService.revokeRefreshTokenByUserId(userId);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }



    @DeleteMapping(value = "{userId}/delete-account")
    @PreAuthorize("#userId == authentication.principal.getId() or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable UUID userId){
        Boolean isDeleted = userService.delete(userId);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @PostMapping("{userId}/logout")
    @PreAuthorize("#userId == authentication.principal.getId()")
    public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String accessToken, @PathVariable UUID userId) {
        Boolean isLoggedOut = false;

        accessToken = accessToken.replace("Bearer ", "");

        revokedTokenService.revokeToken(accessToken);

        String refreshToken = refreshTokenService.getTokenByUserId(userId).getToken();

        revokedTokenService.revokeToken(refreshToken);

        refreshTokenService.revokeRefreshTokenByUserId(userId);


        return new ResponseEntity<>(isLoggedOut, HttpStatus.OK);
    }
}
