package com.scrum.parkingapp.utils;

import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.data.entities.Credential;
import com.scrum.parkingapp.data.entities.LicensePlate;
import com.scrum.parkingapp.data.entities.Owner;
import com.scrum.parkingapp.data.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Driver;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@WithSecurityContext(factory = CustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String name() default "GigiTest";
    String lastname() default "LolloTest";
    String email() default "test@gm.com";
    String role() default "DRIVER";
}






