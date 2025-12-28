package com.kutay.exchange.config.security.JWT;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


@RequiredArgsConstructor
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = ((JWTAuthenticationToken) authentication).getToken();

        String email = jwtUtil.validateTokenAndExtractEmail(token);

        if (email == null) {
            throw new BadCredentialsException("Invalid JWT Token.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

