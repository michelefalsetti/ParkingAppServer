package com.scrum.parkingapp.data.service.implem;


import com.scrum.parkingapp.data.dao.RefreshTokensDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.RefreshToken;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.RefreshTokenService;
import com.scrum.parkingapp.dto.security.RefreshTokenDto;
import com.scrum.parkingapp.exception.TokenNotFoundException;
import com.scrum.parkingapp.exception.UnauthorizedAccessException;
import com.scrum.parkingapp.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokensDao refreshTokenDao;

    private final UsersDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public RefreshTokenDto save(RefreshTokenDto refreshTokenDto, UserDetails userDetails){

        try {
            User user = userDao.findByCredentialEmail(userDetails.getUsername()).orElseThrow(()->new UserNotFoundException("User not found"));

            RefreshToken refreshToken = new RefreshToken();

            refreshToken.setToken(refreshTokenDto.getToken());

            refreshToken.setUser(user);
            
            RefreshToken savedRToken = refreshTokenDao.save(refreshToken);
            return modelMapper.map(savedRToken, RefreshTokenDto.class);
        }catch(Exception e){
            log.error("Unexpected error while saving refresh token for user with email: "+userDetails.getUsername()+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }

    }

    @Override
    public RefreshTokenDto getTokenByUserId(UUID userId) {

        try {
            User user = userDao.findById(userId).orElseThrow(()->new UserNotFoundException("User not found"));

            RefreshToken refreshToken = refreshTokenDao.findByUserId(userId)
                        .orElseThrow(() -> new TokenNotFoundException("Token not found"));

            return modelMapper.map(refreshToken, RefreshTokenDto.class);
        }catch(Exception e){
            log.error("Unexpected error while fetching refresh token by user with ID: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    @Transactional
    public void revokeRefreshTokenByUserId(UUID userId) {
        try {
            refreshTokenDao.deleteByUserId(userId);
        }catch(Exception e){
            log.error("Unexpected error while revoking refresh token for user with ID: "+userId+", "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public RefreshTokenDto findByToken(UUID userId, String token) {
        try {
            RefreshToken refreshToken = refreshTokenDao.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token not found"));

            if(refreshToken.getUser().getId().equals(userId))
                return modelMapper.map(refreshToken, RefreshTokenDto.class);
            else
                throw new UnauthorizedAccessException("Unauthorized");
        }catch(Exception e){
            log.error("Unexpected error while fetching refresh token "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    @Transactional
    public void revokeRefreshTokenByToken(UUID userId, String token) {
        try {
            RefreshToken refreshToken = refreshTokenDao.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token not found"));

            if(refreshToken.getUser().getId().equals(userId))
                refreshTokenDao.deleteByToken(refreshToken.toString());
            else
                throw new UnauthorizedAccessException("Unauthorized");
        }catch(Exception e){
            log.error("Unexpected error while revoking refresh token "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    @Override
    public List<RefreshTokenDto> getAll() {
        try {
            List<RefreshToken> tokens = refreshTokenDao.findAll();
            return tokens.stream().map(token1 -> modelMapper.map(token1, RefreshTokenDto.class)).toList();
        }catch(Exception e){
            log.error("Unexpected error while fetching refresh tokens "+e);
            throw new RuntimeException("Unexpected error occurred");
        }
    }

}
