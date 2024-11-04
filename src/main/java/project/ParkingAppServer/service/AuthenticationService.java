package project.ParkingAppServer.service;

import org.springframework.http.ResponseEntity;
import project.ParkingAppServer.data.entities.User;

import java.time.LocalDate;

public interface AuthenticationService {
    void authenticate(String email, String password);

    ResponseEntity<User> register( String password,
                                   String email,
                                   String nome,
                                   String cognome,
                                   LocalDate datanascita,
                                   String numerotelefono
                                  );

    String getUser(String email);

    Iterable<User> getUsers();
}
