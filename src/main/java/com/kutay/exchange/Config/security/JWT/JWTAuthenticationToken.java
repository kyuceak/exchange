package com.kutay.exchange.Config.security.JWT;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public JWTAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
