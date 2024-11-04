package project.ParkingAppServer.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.ParkingAppServer.data.dao.UserDao;
import project.ParkingAppServer.data.entities.User;

import java.time.LocalDate;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserDao userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(email);
        System.out.println(password);

    }



    @Override
    @Transactional
    public ResponseEntity<User> register(
            String password,
            String email,
            String nome,
            String cognome,
            LocalDate datanascita,
            String numerotelefono) {


        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.status(409).build();
        }

        User userAccount = new User(passwordEncoder.encode(password), email, nome, cognome,datanascita, numerotelefono);
        userRepository.save(userAccount);



        return ResponseEntity.ok(userAccount);
    }

    @Override
    public String getUser(String email) {
        User user = userRepository.findByEmail(email);
        return "{\"email\": \"" + user.getEmail() + "\"}";
    }

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
}
