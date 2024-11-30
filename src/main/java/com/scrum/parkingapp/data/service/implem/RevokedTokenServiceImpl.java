package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.data.dao.RevokedTokensDao;
import com.scrum.parkingapp.data.entities.RevokedToken;
import com.scrum.parkingapp.data.service.RevokedTokenService;
import com.scrum.parkingapp.exception.IsTokenRevokedException;
import com.scrum.parkingapp.exception.RevokingTokenErrorException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class RevokedTokenServiceImpl implements RevokedTokenService {
    @Autowired
    private RevokedTokensDao revokedTokensDao;

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating token hash", e);
        }
    }
    @Transactional
    public void revokeToken(String token) {
        try {
            String hashedToken = hashToken(token);
            if (!isTokenRevoked(token)) {
                RevokedToken revokedToken = new RevokedToken(hashedToken);
                revokedTokensDao.save(revokedToken);
            }
        }catch(Exception e){
            log.error("Unexpected error while converting revoking token "+e);
            throw new RevokingTokenErrorException("Unexpected error occurred");
        }
    }

    public boolean isTokenRevoked(String token) {
        try {
            String hashedToken = hashToken(token);
            return revokedTokensDao.existsByToken(hashedToken);
        }catch(Exception e){
            log.error("Unexpected error while converting revoking token "+e);
            throw new IsTokenRevokedException("Unexpected error occurred");
        }
    }
}
