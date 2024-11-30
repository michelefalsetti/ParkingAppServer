package com.scrum.parkingapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Value("${aes.secret.key}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

}
