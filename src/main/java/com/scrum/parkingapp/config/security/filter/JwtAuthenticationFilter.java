package com.scrum.parkingapp.config.security.filter;


import com.scrum.parkingapp.config.security.JwtService;
import com.scrum.parkingapp.config.security.LoggedUserDetails;
import com.scrum.parkingapp.config.security.LoggedUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final LoggedUserDetailsService userDetailsService;

    private static final int TOO_MANY_REQUESTS = 429;

    // fa il parsing del token e controlla se l'utente è autenticato

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // bearer è il tipo di token
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        String tokenType;
        try {
            tokenType = jwtService.extractClaim(jwt, claims -> claims.get("type", String.class));
        }

        catch (ExpiredJwtException e){
            throw e;
        }

        String requestURI = request.getRequestURI();

        if ("/api/v1/auth/refreshToken".equals(requestURI)) { // Replace with your actual refresh endpoint
            // This endpoint should only accept refresh tokens
            if (!"refresh-token".equals(tokenType)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token type for this endpoint");
                return;
            }
        } else {
            // Other endpoints should accept only access tokens
            if (!"access-token".equals(tokenType)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token type for this endpoint");
                return;
            }
        }

            try{
                jwtService.isTokenValid(jwt);
            }
            catch (ExpiredJwtException e){
                throw e;
            }
            username = jwtService.extractUsername(jwt);
            UUID userId = jwtService.extractUserId(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                LoggedUserDetails userDetails = (LoggedUserDetails) this.userDetailsService.loadUserByUsername(username);
                System.out.println("UserDetails: " + userDetails);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        filterChain.doFilter(request, response);
    }
}
