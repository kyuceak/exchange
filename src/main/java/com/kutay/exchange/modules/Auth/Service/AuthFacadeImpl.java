
package com.kutay.exchange.modules.Auth.Service;

import com.kutay.exchange.modules.Auth.DTO.AuthResponse;
import com.kutay.exchange.modules.Auth.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

    private final AuthService authService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        return authService.register(request);
    }

    @Override
    public AuthResponse login(RegisterRequest request) {
        return authService.login(request);
    }

}