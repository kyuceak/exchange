package com.kutay.exchange.config.security.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/api/auth/generate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication authResult = authenticationManager.authenticate(authToken);
        System.out.println(authResult);

        if (authResult.isAuthenticated()) {
            String token = jwtUtil.generateToken(authResult.getName(), 15);
            response.setHeader("Authorization", "Bearer " + token);


            String refreshToken = jwtUtil.generateToken(authResult.getName(), 7 * 24 * 60);
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/api/auth/refresh-token");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshCookie);
        }
    }
}
