package project.ParkingAppServer.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.ParkingAppServer.data.dao.UserDao;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userRepository;

    public UserDetailsServiceImpl(UserDao userRepository) {
        this.userRepository = userRepository;
    }


    public UserDetails loadUserbyEmail(String email) throws UsernameNotFoundException {

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}