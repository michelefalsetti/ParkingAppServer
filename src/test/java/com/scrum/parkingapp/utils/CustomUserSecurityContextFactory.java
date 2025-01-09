package com.scrum.parkingapp.utils;

import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.data.entities.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Aggiungi il prefisso ROLE_ al ruolo
        String roleWithPrefix = "ROLE_" + customUser.role();

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority(roleWithPrefix)
        );

        User user = null;
        // Controlla il ruolo senza il prefisso ROLE_
        if (customUser.role().equals("OWNER")) {
            user = new Owner();
            System.out.println("User Owner created with role: " + roleWithPrefix);
        } else if (customUser.role().equals("ADMIN")) {
            user = new Admin();
            System.out.println("User Admin created with role: " + roleWithPrefix);
        } else {
            user = new User();
            System.out.println("Regular User created with role: " + roleWithPrefix);
        }

        user.setId(UUID.randomUUID());
        user.setFirstName(customUser.name());
        user.setLastName(customUser.lastname());

        Credential credential = new Credential();
        credential.setEmail(customUser.email());
        credential.setPassword("Ciaobello!10");
        user.setCredential(credential);
        user.setBirthDate(LocalDate.of(1999, 1, 1));

        // Creare LoggedUserDetails con l'utente e le autorità corrette
        LoggedUserDetails principal = new LoggedUserDetails(user);

        // Assicurati che le autorità siano correttamente impostate
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                credential,
                authorities
        );

        // Debug output
        System.out.println("Created authentication with authorities: " +
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", ")));

        context.setAuthentication(auth);
        return context;
    }
}
