package project.ParkingAppServer.security.config;



import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.ParkingAppServer.security.RequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration{
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private final RequestFilter requestFilter;

    public SecurityConfiguration(RequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.
                authorizeHttpRequests().requestMatchers("/api/v1/register", "/api/v1/authenticate").permitAll().and().
                authorizeHttpRequests().anyRequest().authenticated().and().csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class).build();

    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleRequestNotPermitted(RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests, please try again later.");
    }
}