package com.scrum.parkingapp.config.security;

import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.exception.InvalidCredentialException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoggedUserDetailsService implements UserDetailsService {

    @Autowired //indica che il campo può essere iniettato
    private UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersDao.findByCredentialEmail(email).orElseThrow(() -> new InvalidCredentialException("Invalid credentials"));
        return new LoggedUserDetails(user);

    }

}
