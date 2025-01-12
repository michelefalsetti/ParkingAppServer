package com.scrum.parkingapp.config.security;

import com.scrum.parkingapp.config.ExceptionHandlerFilter;
import com.scrum.parkingapp.config.security.filter.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final LoggedUserDetailsService userDetailsService;
        private final ExceptionHandlerFilter ExceptionHandlerFilter;



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable).exceptionHandling(AbstractHttpConfigurer::disable)
                    .exceptionHandling(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> {auth
                            .requestMatchers("/api/v1/auth/**").permitAll();
                    auth.requestMatchers("/api/v1/paymentMethods/add").authenticated();
                    auth.requestMatchers("/api/v1/paymentMethods/get/{userId}").authenticated();
                    auth.requestMatchers("/api/v1/paymentMethods/delete/{paymentMethodId}/{userId}").authenticated();
                    auth.requestMatchers("/api/v1/paymentMethods/get/{userId}/{paymentMethodId}").authenticated();
                    auth.requestMatchers("/api/v1/users/**").authenticated();
                    auth.requestMatchers("/api/v1/addresses/**").authenticated();
                    auth.requestMatchers("api/v1/admin/all-tokens").authenticated();
                    auth.requestMatchers("api/v1/admin/all-users").authenticated();
                    auth.requestMatchers("api/v1/admin/register").permitAll();
                    auth.requestMatchers("/error").permitAll();


                    auth.requestMatchers("/api/v1/parkingSpaces/**").authenticated();
                    auth.requestMatchers("/api/v1/parkingSpots/**").authenticated();
                    auth.requestMatchers("/api/v1/reservations/**").authenticated();

                    auth.requestMatchers("/api/v1/shopping-cart/get/total/**").authenticated();
                                auth.requestMatchers("/api/v1/transactions/**").authenticated();
                                auth.requestMatchers("/swagger-ui/**").permitAll();
                                auth.requestMatchers("/swagger-ui.html").permitAll();
                                auth.requestMatchers("/v3/api-docs/**").permitAll();
                    }
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(ExceptionHandlerFilter, JwtAuthenticationFilter.class);



            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() { //serve per abilitare le chiamate da localhost
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("https://localhost:*","https://192.168.1.50:*", "https://10.0.2.2:*",
                    "https://*", "https://" + getLocalIPv4Address()+ ":*" )); // Modifica secondo le tue esigenze
            //configuration.setAllowedOrigins(Arrays.asList("https://localhost:8081","https://192.168.1.54:8081", "https://93.44.97.32")); // Modifica secondo le tue esigenze

            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
        }

        public static String getLocalIPv4Address() {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    // Filtra le interfacce di loopback e quelle non attive
                    if (iface.isLoopback() || !iface.isUp()) {
                        continue;
                    }

                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    InetAddress addr = null;
                    while (addresses.hasMoreElements()) {
                        addr = addresses.nextElement();
                        // Filtra gli indirizzi IPv6 e quelli di loopbackif (addr.isLoopbackAddress() || addr.getAddress().length != 4) {
                        continue;
                    }
                    // Restituisce il primo indirizzo IPv4 locale trovato
                    return addr.getHostAddress();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            return null; // Restituisce null se non trova un indirizzo IPv4
        }
}