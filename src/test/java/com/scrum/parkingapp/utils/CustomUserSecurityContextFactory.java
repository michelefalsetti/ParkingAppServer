package com.scrum.parkingapp.utils;

import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.data.entities.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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


        List<GrantedAuthority> authority = Arrays.asList(
                new SimpleGrantedAuthority(customUser.role())
        );

        User user = new User();
        if (authority.equals("OWNER")) {
            user = new Owner();
        }else if (authority.equals("ADMIN")){
            user = new Admin();
        }
        user.setId(UUID.randomUUID());
        user.setFirstName(customUser.name());
        user.setLastName(customUser.lastname());
        Credential credential = new Credential();
        credential.setEmail(customUser.email());
        credential.setPassword("Ciaobello!10");
        user.setCredential(credential);
        user.setBirthDate(LocalDate.of(1999, 1, 1));


        UserDetails principal = new LoggedUserDetails(
                user

        );

        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, user.getCredential(), authority));
        return context;
    }
}
