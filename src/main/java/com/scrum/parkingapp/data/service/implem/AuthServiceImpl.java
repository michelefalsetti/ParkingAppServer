package com.scrum.parkingapp.data.service.implem;


import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.Admin;
import com.scrum.parkingapp.data.entities.Owner;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.AuthService;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.dto.CredentialDto;
import com.scrum.parkingapp.dto.SaveUserDto;
import com.scrum.parkingapp.dto.UserDetailsDto;
import com.scrum.parkingapp.dto.security.AccessTokenValidationDto;
import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import com.scrum.parkingapp.exception.UserAlreadyExistsException;
import com.scrum.parkingapp.exception.UserNotFoundException;
import com.scrum.parkingapp.exception.LoginException;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersDao userDao;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final RevokedTokenService revokedTokenService;

    private final Integer expirationTimeInHours = 96;


    @Override
    public UserDetailsDto registerUser(@Valid SaveUserDto userDto) {
        System.out.println("UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredential().getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });


        String hashedPassword = passwordEncoder.encode(userDto.getCredential().getPassword());

        User user = modelMapper.map(userDto, User.class);
        user.getCredential().setPassword(hashedPassword);
        System.out.println("User: " + user);

        User savedUser = userDao.save(user);

        return modelMapper.map(savedUser, UserDetailsDto.class);
    }

    @Override
    public UserDetailsDto registerOwner(SaveUserDto userDto) {
        System.out.println("Owner UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredential().getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredential().getPassword());

        Owner owner = modelMapper.map(userDto, Owner.class);
        owner.getCredential().setPassword(hashedPassword);
        System.out.println("Owner: " + owner);

        userDao.save(owner);

        return modelMapper.map(owner, UserDetailsDto.class);
    }

    @Override
    public UserDetailsDto registerAdmin(@Valid SaveUserDto userDto) {
        System.out.println("Admin UserDto: " + userDto);

        userDao.findByCredentialEmail(userDto.getCredential().getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this email already exists");
        });

        String hashedPassword = passwordEncoder.encode(userDto.getCredential().getPassword());

        Admin admin = modelMapper.map(userDto, Admin.class);
        admin.getCredential().setPassword(hashedPassword);
        System.out.println("Admin: " + admin);

        userDao.save(admin);

        return modelMapper.map(admin, UserDetailsDto.class);
    }

    @Override
    public Map<String, String> loginUser(CredentialDto loginDto) {

        try{

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            );

            Authentication auth = authenticationManager.authenticate(authToken);



           LoggedUserDetails userDetails = (LoggedUserDetails) auth.getPrincipal();



        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails, expirationTimeInHours);

        RefreshTokenDto r = new RefreshTokenDto(refreshToken);
        refreshTokenService.save(r, userDetails);

        return Map.of("access_token", accessToken, "refresh_token", refreshToken);

    } catch (Exception e) {

        throw new LoginException("Invalid credentials");
    }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, String> refreshToken(String authorizationHeader, String toString) {

        String refreshToken = authorizationHeader.substring("Bearer ".length());
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            authenticationToken = jwtService.parseToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String username = authenticationToken.getName();
        User user = userDao.findByCredentialEmail(username).orElseThrow(()->new RuntimeException("user not found"));
        LoggedUserDetails loggedUserDetails = new LoggedUserDetails(user);
        String accessToken = jwtService.generateToken(loggedUserDetails);
        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
    }

    @Override
    public AccessTokenValidationDto validateToken(AccessTokenValidationDto accessTokenValidationDto) {
            try {

                jwtService.isTokenValid(accessTokenValidationDto.getToken());

                if (revokedTokenService.isTokenRevoked(accessTokenValidationDto.getToken())) {
                    throw new JwtException("Token is revoked");
                }

                UUID userId = jwtService.extractUserId(accessTokenValidationDto.getToken());

                userDao.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));


                return accessTokenValidationDto;
            } catch (JwtException e) {
                throw new JwtException("Token is invalid");
            }
        }

    }

