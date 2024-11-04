package project.ParkingAppServer.controller;

import com.nimbusds.jose.JOSEException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.ParkingAppServer.data.entities.User;
import project.ParkingAppServer.security.TokenStore;
import project.ParkingAppServer.service.AuthenticationService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(path="/api/v1", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/authenticate")
    @RateLimiter(name = "standardRateLimiter")
    @ResponseStatus(HttpStatus.OK)
    public void authenticate(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             HttpServletResponse response) throws JOSEException {
        authenticationService.authenticate(email, password);
        String token = TokenStore.getInstance().createToken(Map.of("email",email));
        response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ token);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("datadinascita") LocalDate datadinascita,
            @RequestParam("numeroditelefono") String numeroditelefono

    ) {
        return authenticationService.register(password, email, nome, cognome,datadinascita,numeroditelefono);
    }

    @GetMapping(path="/users/{email}")
    @PreAuthorize("#email.equals(authentication.principal.email) or hasRole('ADMIN')")
    public String getUser(@PathVariable("email") String email) {
        return authenticationService.getUser(email);
    }

    @GetMapping(path="/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getUsers() {
        return authenticationService.getUsers();
    }
}
