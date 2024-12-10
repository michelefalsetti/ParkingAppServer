package com.scrum.parkingapp.config.security;


import com.scrum.parkingapp.data.entities.User;
import jakarta.persistence.DiscriminatorValue;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
public class LoggedUserDetails implements UserDetails{

    @Getter
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    private String phoneNumber;
    private List<GrantedAuthority> authorities;

    public LoggedUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getCredential().getEmail();
        this.password = user.getCredential().getPassword();
        this.authorities = Collections.singletonList( new SimpleGrantedAuthority("ROLE_" + user.getClass().getAnnotation(DiscriminatorValue.class).value()));
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthDate = user.getBirthDate();
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}