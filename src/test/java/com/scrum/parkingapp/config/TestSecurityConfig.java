package com.scrum.parkingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
;  // Importa questa se stai usando una versione precedente a 5.0


@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disabilita CSRF
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()  // Permetti senza autenticazione
                        .requestMatchers("/api/v1/parkingSpaces/**").authenticated()
                        .requestMatchers("/api/v1/parkingSpots/**").authenticated()
                        .requestMatchers("/api/v1/reservations/**").authenticated()

                        .anyRequest().authenticated())  // Richiedi autenticazione per tutte le altre richieste
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // Usa la gestione stateless (adatta per API REST)

        return http.build();  // Costruisce la configurazione finale
    }
}
